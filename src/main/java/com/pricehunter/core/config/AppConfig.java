package com.pricehunter.core.config;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;

@Configuration
@EnableCaching
@Slf4j
public class AppConfig implements WebMvcConfigurer {

    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 1000;
    private static final String ASYNC_PREFIX = "async-";
    private static final boolean WAIT_FOR_TASK_TO_COMPLETE_ON_SHUTDOWN = true;

    private final TraceSleuthInterceptor traceSleuthInterceptor;
    private final BeanFactory beanFactory;
    private final boolean isSentinelEnabled;
    private final RedisProperties redisProperties;

    public AppConfig(
      TraceSleuthInterceptor traceSleuthInterceptor,
      BeanFactory beanFactory,
      @Value("${cache.redis.sentinel:false}") final Boolean isSentinelEnabled,
      RedisProperties redisProperties) {
        this.traceSleuthInterceptor = traceSleuthInterceptor;
        this.beanFactory = beanFactory;
        this.isSentinelEnabled = isSentinelEnabled;
        this.redisProperties = redisProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.traceSleuthInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Bean("asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASK_TO_COMPLETE_ON_SHUTDOWN);
        executor.setThreadNamePrefix(ASYNC_PREFIX);
        executor.initialize();

        return new LazyTraceExecutor(beanFactory, executor);
    }

    /**
     * Se utiliza prototype ya que para cada instancia de adapter requerimos tener un RestTemplate unico
     * debido a que el ErrorHandler utiliza uno especifica para cada adapter con sus propias excepciones.
     *
     * @param restTemplateBuilder Builder
     * @return RestTemplate
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                        @Value("${rest.client.default.timeout}") int timeout) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(timeout))
                .setReadTimeout(Duration.ofMillis(timeout))
                .interceptors(new LogRestTemplateInterceptor())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {

      if (isSentinelEnabled && Objects.nonNull(redisProperties.getSentinel())) {
        final RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(
          redisProperties.getSentinel().getMaster(), new HashSet<>(redisProperties.getSentinel().getNodes()));

        return new JedisConnectionFactory(redisSentinelConfiguration);
      }

      RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
      standaloneConfig.setPassword(Optional
        .ofNullable(redisProperties.getPassword())
        .map(RedisPassword::of)
        .orElse(RedisPassword.none()));
      return new JedisConnectionFactory(standaloneConfig);
    }

    @Bean
    @Primary
    public RedisCacheManager cacheManager(
      @Value("${redis.cache.ttl.days:1}") int defaultTtl,
      @Value("${redis.cache.ttl.product.hours}") int productTTL,
      @Qualifier("jedisConnectionFactory")
        RedisConnectionFactory connectionFactory) {
      val cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofDays(defaultTtl));

      val productCache = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofHours(productTTL));

      return RedisCacheManager
        .builder(connectionFactory)
        .cacheDefaults(cacheConfig)
        .withCacheConfiguration("productCache", productCache)
        .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
      RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setKeySerializer(new StringRedisSerializer());
      template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
      template.setConnectionFactory(jedisConnectionFactory());
      return template;
    }
}
