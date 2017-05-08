package com.prolificinteractive.geocoder;

import java.util.ArrayList;
import java.util.List;

public class GeocoderBuilder {
  private final List<GeocodingApi> geocodingApis = new ArrayList<>();
  private Downloader.Factory downloaderFactory;
  private SwitchPolicy switchPolicy;

  public GeocoderBuilder addGeocodingApi(final GeocodingApi geocodingApi) {
    if (geocodingApi != null) {
      this.geocodingApis.add(geocodingApi);
    }

    return this;
  }

  public Geocoder build() {
    return new Geocoder(downloaderFactory, switchPolicy, geocodingApis);
  }

  public GeocoderBuilder setDownloaderFactory(final Downloader.Factory downloaderFactory) {
    this.downloaderFactory = downloaderFactory;
    return this;
  }

  public GeocoderBuilder setSwitchPolicy(final SwitchPolicy switchPolicy) {
    this.switchPolicy = switchPolicy;
    return this;
  }
}
