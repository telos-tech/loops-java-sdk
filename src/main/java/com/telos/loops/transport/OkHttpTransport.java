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

public class OkHttpTransport implements Transport {

  private final OkHttpClient client;

  public OkHttpTransport() {
    this(new OkHttpClient());
  }

  public OkHttpTransport(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public TransportResponse execute(TransportRequest request) {
    Request okHttpRequest = buildRequest(request);
    try (Response response = client.newCall(okHttpRequest).execute()) {
      return buildResponse(response);
    } catch (IOException e) {
      throw new RuntimeException("Failed into execute request", e);
    }
  }

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
