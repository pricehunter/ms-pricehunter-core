package com.pricehunter.core.config;

import brave.Tracer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pricehunter.core.adapter.rest.exception.EmptyOrNullBodyRestClientException;
import com.pricehunter.core.adapter.rest.exception.NonTargetRestClientException;
import com.pricehunter.core.adapter.rest.exception.NotFoundRestClientException;
import com.pricehunter.core.adapter.rest.exception.RestClientGenericException;
import com.pricehunter.core.adapter.rest.exception.TimeoutRestClientException;
import com.pricehunter.core.config.exception.ProductNotFoundException;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    private static final String X_B3_TRACE_ID = "X-B3-TraceId";
    private static final String X_B3_SPAN_ID = "X-B3-SpanId";
    private static final String PROD_PROFILE = "prod";
    private final HttpServletRequest httpServletRequest;
    private final Tracer tracer;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    public ErrorHandler(final HttpServletRequest httpServletRequest,
                        final Tracer tracer) {
        this.httpServletRequest = httpServletRequest;
        this.tracer = tracer;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handle(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(NotFoundRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotFoundRestClientException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(TimeoutRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(TimeoutRestClientException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.REQUEST_TIMEOUT, ex, ex.getCode());
    }

    @ExceptionHandler(EmptyOrNullBodyRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(EmptyOrNullBodyRestClientException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(NonTargetRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(NonTargetRestClientException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler(RestClientGenericException.class)
    public ResponseEntity<ApiErrorResponse> handle(RestClientGenericException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(ProductNotFoundException ex) {
      log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
      return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
      log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
      return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(HttpStatus httpStatus, Throwable ex, ErrorCode errorCode) {

        final var debugMessage = Optional.of(activeProfile)
                .filter(profile -> profile.contains(PROD_PROFILE))
                .map(i -> ex.getLocalizedMessage())
                .orElse(Arrays.toString(ex.getStackTrace()));

        final var traceId = Optional.ofNullable(this.tracer.currentSpan())
                .map(span -> span.context().traceIdString())
                .orElse(TraceSleuthInterceptor.TRACE_ID_NOT_EXISTS);

        final var spandId = Optional.ofNullable(this.tracer.currentSpan())
                .map(span -> span.context().spanIdString())
                .orElse(TraceSleuthInterceptor.SPAND_ID_NOT_EXISTS);


        final var queryString = Optional.ofNullable(httpServletRequest.getQueryString())
                .orElse("");


        final var metaData = Map.of(X_B3_TRACE_ID, traceId,
                X_B3_SPAN_ID, spandId,
                "query_string", queryString,
                "stack_trace", debugMessage);

        final var apiErrorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .name(httpStatus.getReasonPhrase())
                .detail(String.format("%s: %s", ex.getClass().getCanonicalName(), ex.getMessage()))
                .status(httpStatus.value())
                .code(errorCode.value())
                .id("")
                .resource(httpServletRequest.getRequestURI())
                .metadata(metaData)
                .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @Builder
    @NonNull
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class ApiErrorResponse {

        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']";

        @JsonProperty
        private String name;
        @JsonProperty
        private Integer status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        private LocalDateTime timestamp;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private String resource;
        @JsonProperty
        private String id;
        @JsonProperty
        private String detail;
        @JsonProperty
        private Map<String, String> metadata;
    }
}

