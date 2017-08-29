package com.prolificinteractive.geocoder.api.okhttp;

import com.prolificinteractive.geocoder.Downloader;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class OkHttpDownloader implements Downloader {

  private static final String HEADER_REFERER = "Referer";
  private static final String HEADER_USER_AGENT = "User-Agent";

  private final OkHttpClient client;

  private Call call;

  private OkHttpDownloader(final OkHttpClient client) {
    this.client = client;
  }

  @Override public Disposable getDisposable() {
    return new Disposable() {
      @Override public void dispose() {
        if (call != null) {
          call.cancel();
        }
      }

      @Override public boolean isDisposed() {
        return call != null && call.isCanceled();
      }
    };
  }

  @Override public byte[] request(final String url) throws IOException {
    call = client
        .newCall(new Request.Builder()
            .url(url)
            .addHeader(
                HEADER_USER_AGENT,
                System.getProperty("http.agent")
            )
            .addHeader(
                HEADER_REFERER,
                "https://play.google.com/store/apps/details?id=com.scotts.gro&hl=en"
            )
            .get()
            .build());

    return call
        .execute()
        .body()
        .bytes();
  }

  public static class OkHttpFactory extends Factory {

    private final OkHttpClient client;

    public OkHttpFactory(final OkHttpClient client) {
      this.client = client;
    }

    @Override public Downloader create() {
      return new OkHttpDownloader(client);
    }
  }
}