package com.prolificinteractive.geocoder;

import io.reactivex.disposables.Disposable;
import java.io.IOException;

public interface Downloader {
  Disposable getDisposable();

  byte[] request(final String url) throws IOException;

  abstract class Factory {
    public abstract Downloader create();
  }
}