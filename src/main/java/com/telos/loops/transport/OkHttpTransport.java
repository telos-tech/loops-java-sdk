package com.telos.loops.transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Default HTTP transport implementation using OkHttp.
 *
 * <p>This is the default {@link Transport} implementation used by the Loops SDK. It provides
 * reliable HTTP communication with modern features like HTTP/2 support, connection pooling, and
 * efficient resource management.
 *
 * <h2>Features</h2>
 *
 * <ul>
 *   <li>HTTP/2 and HTTP/1.1 support
 *   <li>Connection pooling and reuse
 *   <li>Automatic GZIP compression
 *   <li>Response caching (when configured)
 *   <li>Proxy support
 *   <li>Cookie handling
 * </ul>
 *
 * <h2>Default Usage</h2>
 *
 * <p>The SDK automatically uses this transport with a default OkHttp client:
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 * // OkHttpTransport is used internally
 * }</pre>
 *
 * <h2>Custom OkHttpClient</h2>
 *
 * <p>For advanced use cases, you can create a transport with a customized OkHttpClient:
 *
 * <pre>{@code
 * // Create custom OkHttpClient with timeout configuration
 * OkHttpClient customClient = new OkHttpClient.Builder()
 *     .connectTimeout(30, TimeUnit.SECONDS)
 *     .readTimeout(60, TimeUnit.SECONDS)
 *     .writeTimeout(60, TimeUnit.SECONDS)
 *     .build();
 *
 * OkHttpTransport transport = new OkHttpTransport(customClient);
 * // Note: Custom transport injection not yet available in LoopsClient
 * }</pre>
 *
 * <h2>Proxy Configuration</h2>
 *
 * <pre>{@code
 * // Configure HTTP proxy
 * Proxy proxy = new Proxy(Proxy.Type.HTTP,
 *     new InetSocketAddress("proxy.example.com", 8080));
 *
 * OkHttpClient proxyClient = new OkHttpClient.Builder()
 *     .proxy(proxy)
 *     .build();
 *
 * OkHttpTransport transport = new OkHttpTransport(proxyClient);
 * }</pre>
 *
 * <h2>Request Logging</h2>
 *
 * <pre>{@code
 * // Add HTTP logging interceptor
 * HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
 * logging.setLevel(HttpLoggingInterceptor.Level.BODY);
 *
 * OkHttpClient loggingClient = new OkHttpClient.Builder()
 *     .addInterceptor(logging)
 *     .build();
 *
 * OkHttpTransport transport = new OkHttpTransport(loggingClient);
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>This class is thread-safe and can be used concurrently from multiple threads. The underlying
 * OkHttpClient manages connection pooling and thread safety internally.
 *
 * @see Transport
 * @see TransportRequest
 * @see TransportResponse
 */
public class OkHttpTransport implements Transport {

  private final OkHttpClient client;

  /**
   * Creates a new OkHttpTransport with a default OkHttpClient.
   *
   * <p>The default client uses OkHttp's default configuration with sensible timeouts and
   * connection pooling.
   */
  public OkHttpTransport() {
    this(new OkHttpClient());
  }

  /**
   * Creates a new OkHttpTransport with a custom OkHttpClient.
   *
   * <p>Use this constructor to provide a customized OkHttpClient with specific timeouts, proxy
   * settings, interceptors, or other configuration.
   *
   * @param client the OkHttpClient to use for HTTP communication
   */
  public OkHttpTransport(OkHttpClient client) {
    this.client = client;
  }

  /**
   * Executes an HTTP request synchronously using OkHttp.
   *
   * <p>This method blocks until the HTTP request completes and a response is received. The
   * response is automatically closed after reading.
   *
   * @param request the HTTP request to execute
   * @return the HTTP response containing status code, headers, and body
   * @throws RuntimeException if the request fails due to network error, timeout, or I/O issues
   */
  @Override
  public TransportResponse execute(TransportRequest request) {
    Request okHttpRequest = buildRequest(request);
    try (Response response = client.newCall(okHttpRequest).execute()) {
      return buildResponse(response);
    } catch (IOException e) {
      throw new RuntimeException("Failed into execute request", e);
    }
  }

  /**
   * Executes an HTTP request asynchronously using OkHttp.
   *
   * <p>This method returns immediately with a {@link CompletableFuture} that will be completed
   * when the HTTP request finishes. The response is automatically closed after reading.
   *
   * <p>The request is executed on OkHttp's internal thread pool. The CompletableFuture is
   * completed on the same thread that receives the response.
   *
   * @param request the HTTP request to execute
   * @return a CompletableFuture that will complete with the HTTP response, or complete
   *     exceptionally if the request fails
   */
  @Override
  public CompletableFuture<TransportResponse> executeAsync(TransportRequest request) {
    CompletableFuture<TransportResponse> future = new CompletableFuture<>();
    Request okHttpRequest = buildRequest(request);

    client
        .newCall(okHttpRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                try {
                  future.complete(buildResponse(response));
                } catch (Exception e) {
                  future.completeExceptionally(e);
                } finally {
                  response.close();
                }
              }
            });

    return future;
  }

  private Request buildRequest(TransportRequest request) {
    Request.Builder builder = new Request.Builder().url(request.url());

    if (request.headers() != null) {
      request.headers().forEach(builder::header);
    }

    RequestBody body = null;
    if (request.body() != null) {
      MediaType contentType = MediaType.parse("application/json; charset=utf-8");
      if (request.headers() != null && request.headers().containsKey("Content-Type")) {
        contentType = MediaType.parse(request.headers().get("Content-Type"));
      }
      body = RequestBody.create(request.body(), contentType);
    }

    switch (request.httpMethod()) {
      case GET -> builder.get();
      case POST -> builder.post(body != null ? body : RequestBody.create(new byte[0], null));
      case PUT -> builder.put(body != null ? body : RequestBody.create(new byte[0], null));
      case DELETE -> {
        if (body != null) {
          builder.delete(body);
        } else {
          builder.delete();
        }
      }
    }

    return builder.build();
  }

  private TransportResponse buildResponse(Response response) throws IOException {
    Map<String, String> headers = new HashMap<>();
    for (String name : response.headers().names()) {
      headers.put(name, response.header(name));
    }

    byte[] responseBody = new byte[0];
    ResponseBody body = response.body();
    if (body != null) {
      responseBody = body.bytes();
    }

    return new TransportResponse(response.code(), headers, responseBody);
  }
}
