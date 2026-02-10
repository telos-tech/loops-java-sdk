package com.telos.loops.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.error.RateLimitExceededException;
import com.telos.loops.model.RequestOptions;
import com.telos.loops.transport.HttpMethod;
import com.telos.loops.transport.Transport;
import com.telos.loops.transport.TransportRequest;
import com.telos.loops.transport.TransportResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreSender {
  private static final Logger logger = LoggerFactory.getLogger(CoreSender.class);
  private final Transport transport;
  private final ObjectMapper objectMapper;
  private final String baseUrl;
  private final String apiKey;

  public CoreSender(Transport transport, String baseUrl, String apiKey) {
    this.transport = transport;
    this.objectMapper = new ObjectMapper();
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
  }

  // ============================================================
  // POST Methods (JSON Body)
  // ============================================================

  public <T> T postJson(
      String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequest(HttpMethod.POST, path, request, null, responseType, options);
  }

  public <T> CompletableFuture<T> postJsonAsync(
      String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequestAsync(HttpMethod.POST, path, request, null, responseType, options);
  }

  // ============================================================
  // PUT Methods (JSON Body)
  // ============================================================

  public <T> T putJson(String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequest(HttpMethod.PUT, path, request, null, responseType, options);
  }

  public <T> CompletableFuture<T> putJsonAsync(
      String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequestAsync(HttpMethod.PUT, path, request, null, responseType, options);
  }

  // ============================================================
  // GET Methods (Query Parameters)
  // ============================================================

  public <T> T get(
      String path, Map<String, String> queryParams, Class<T> responseType, RequestOptions options) {
    return executeRequest(HttpMethod.GET, path, null, queryParams, responseType, options);
  }

  public <T> CompletableFuture<T> getAsync(
      String path, Map<String, String> queryParams, Class<T> responseType, RequestOptions options) {
    return executeRequestAsync(HttpMethod.GET, path, null, queryParams, responseType, options);
  }

  public <T> List<T> getList(
      String path,
      Map<String, String> queryParams,
      TypeReference<List<T>> typeRef,
      RequestOptions options) {
    return executeRequestList(HttpMethod.GET, path, null, queryParams, typeRef, options);
  }

  public <T> CompletableFuture<List<T>> getListAsync(
      String path,
      Map<String, String> queryParams,
      TypeReference<List<T>> typeRef,
      RequestOptions options) {
    return executeRequestListAsync(HttpMethod.GET, path, null, queryParams, typeRef, options);
  }

  // ============================================================
  // DELETE Methods (JSON Body)
  // ============================================================

  public <T> T deleteJson(
      String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequest(HttpMethod.DELETE, path, request, null, responseType, options);
  }

  public <T> CompletableFuture<T> deleteJsonAsync(
      String path, Object request, Class<T> responseType, RequestOptions options) {
    return executeRequestAsync(HttpMethod.DELETE, path, request, null, responseType, options);
  }

  // ============================================================
  // Core Execution Methods
  // ============================================================

  private <T> T executeRequest(
      HttpMethod method,
      String path,
      Object requestBody,
      Map<String, String> queryParams,
      Class<T> responseType,
      RequestOptions options) {
    try {
      TransportRequest transportRequest =
          buildTransportRequest(method, path, requestBody, queryParams, options);
      logRequest(transportRequest);

      TransportResponse response = transport.execute(transportRequest);
      logResponse(response);

      validateResponse(response);

      return deserializeResponse(response, responseType);
    } catch (LoopsApiException e) {
      throw e;
    } catch (Exception e) {
      throw new LoopsApiException("Request failed: " + e.getMessage());
    }
  }

  private <T> CompletableFuture<T> executeRequestAsync(
      HttpMethod method,
      String path,
      Object requestBody,
      Map<String, String> queryParams,
      Class<T> responseType,
      RequestOptions options) {
    return CompletableFuture.supplyAsync(
        () -> executeRequest(method, path, requestBody, queryParams, responseType, options));
  }

  private <T> List<T> executeRequestList(
      HttpMethod method,
      String path,
      Object requestBody,
      Map<String, String> queryParams,
      TypeReference<List<T>> typeRef,
      RequestOptions options) {
    try {
      TransportRequest transportRequest =
          buildTransportRequest(method, path, requestBody, queryParams, options);
      logRequest(transportRequest);

      TransportResponse response = transport.execute(transportRequest);
      logResponse(response);

      validateResponse(response);

      return objectMapper.readValue(response.response(), typeRef);
    } catch (LoopsApiException e) {
      throw e;
    } catch (Exception e) {
      throw new LoopsApiException("Request failed: " + e.getMessage());
    }
  }

  private <T> CompletableFuture<List<T>> executeRequestListAsync(
      HttpMethod method,
      String path,
      Object requestBody,
      Map<String, String> queryParams,
      TypeReference<List<T>> typeRef,
      RequestOptions options) {
    return CompletableFuture.supplyAsync(
        () -> executeRequestList(method, path, requestBody, queryParams, typeRef, options));
  }

  private void validateResponse(TransportResponse response) {
    if (response.status() >= 400) {
      String rawBody = new String(response.response(), StandardCharsets.UTF_8);

      if (response.status() == 429) {
        long retryAfter = 0;
        String retryAfterHeader = response.headers().get("Retry-After");
        if (retryAfterHeader != null) {
          try {
            retryAfter = Long.parseLong(retryAfterHeader);
          } catch (NumberFormatException e) {
            // Ignore parse error
          }
        }
        throw new RateLimitExceededException(response.status(), rawBody, retryAfter);
      }

      throw new LoopsApiException(response.status(), rawBody);
    }
  }

  private void logRequest(TransportRequest request) {
    if (logger.isDebugEnabled()) {
      logger.debug(
          "--> {} {} {}",
          request.httpMethod(),
          request.url(),
          request.body().length > 0 ? "(" + request.body().length + "-byte body)" : "");
    } else {
      logger.info("--> {} {}", request.httpMethod(), request.url());
    }
  }

  private void logResponse(TransportResponse response) {
    boolean isSuccess = response.status() >= 200 && response.status() < 300;
    String status = response.status() + (isSuccess ? "" : " ERROR");

    if (logger.isDebugEnabled()) {
      logger.debug("<-- {} ({}-byte body)", status, response.response().length);
    } else if (!isSuccess) {
      logger.warn("<-- {}", status);
    } else {
      logger.info("<-- {}", status);
    }
  }

  private TransportRequest buildTransportRequest(
      HttpMethod method,
      String path,
      Object requestBody,
      Map<String, String> queryParams,
      RequestOptions options)
      throws Exception {
    String url = baseUrl + path;

    // Add query parameters to URL
    if (queryParams != null && !queryParams.isEmpty()) {
      StringBuilder queryString = new StringBuilder("?");
      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        if (queryString.length() > 1) {
          queryString.append("&");
        }
        queryString
            .append(entry.getKey())
            .append("=")
            .append(java.net.URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
      }
      url += queryString.toString();
    }

    // Build headers
    Map<String, String> headers = new HashMap<>(options.headers());
    headers.put("Authorization", "Bearer " + apiKey);
    if (requestBody != null) {
      headers.put("Content-Type", "application/json");
    }

    // Serialize request body
    byte[] body = new byte[0];
    if (requestBody != null) {
      body = objectMapper.writeValueAsBytes(requestBody);
    }

    return new TransportRequest(method, url, headers, body);
  }

  private <T> T deserializeResponse(TransportResponse response, Class<T> responseType)
      throws Exception {
    if (responseType == Void.class) {
      return null;
    }
    return objectMapper.readValue(response.response(), responseType);
  }
}
