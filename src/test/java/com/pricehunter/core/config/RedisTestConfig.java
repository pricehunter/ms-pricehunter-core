package com.pricehunter.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class RedisTestConfig {

  private RedisServer redisServer;

  public RedisTestConfig(RedisProperties redisProperties) {
    this.redisServer = new RedisServer(redisProperties.getPort());
  }

  @PostConstruct
  public void postConstruct() {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    redisServer.stop();
  }
}
