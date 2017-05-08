package com.prolificinteractive.geocoder.api.googlemaps;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.prolificinteractive.geocoder.Downloader;
import com.prolificinteractive.geocoder.GeocodingApi;
import com.prolificinteractive.geocoder.RetriableException;
import com.prolificinteractive.geocoder.model.Address;
import java.util.List;

public class GoogleMaps implements GeocodingApi {
  private static final String API_NAME = "Google Maps Api";
  private static final String ENDPOINT_URL = "https://maps.googleapis.com/maps/api/geocode/json";
  private static final int MAX_RESULTS = 5;

  @Nullable
  private final String mApiKey;

  private GoogleMaps(@Nullable final String mApiKey) {
    this.mApiKey = mApiKey;
  }

  public static GoogleMaps create(final String mApiKey) {
    return new GoogleMaps(mApiKey);
  }

  @Override public List<Address> convert(final List<?> addresses) {
    return (List<Address>) addresses;
  }

  @Override public List<Address> coordinateCall(
      final Downloader downloader,
      final double latitude,
      final double longitude)
      throws Exception {

    final Uri.Builder uriBuilder = buildBaseRequestUri()
        .appendQueryParameter("latlng", latitude + "," + longitude);

    final byte[] data = downloader.request(uriBuilder.toString());

    try {
      return Parser.parseJson(data, MAX_RESULTS, true);
    } catch (final GoogleMapsException e) {
      if (e.getStatus() == Status.OVER_QUERY_LIMIT) {
        // OVER_QUERY_LIMIT is is an error that is eligible for retrying.
        throw new RetriableException(e.toString());
      } else {
        throw e;
      }
    }
  }

  @Override public String getName() {
    return API_NAME;
  }

  @Override public List<Address> locationCall(
      final Downloader downloader,
      final String locationName)
      throws Exception {

    final Uri.Builder uriBuilder = buildBaseRequestUri()
        .appendQueryParameter("address", locationName);

    final String url = uriBuilder.toString();
    final byte[] data = downloader.request(url);

    try {
      return Parser.parseJson(data, MAX_RESULTS, true);
    } catch (final GoogleMapsException e) {
      if (e.getStatus() == Status.OVER_QUERY_LIMIT) {
        throw new RetriableException(e.toString());
      } else {
        throw e;
      }
    }
  }

  @NonNull
  private Uri.Builder buildBaseRequestUri() {
    final Uri.Builder uriBuilder = Uri.parse(ENDPOINT_URL).buildUpon();
    if (mApiKey != null && !mApiKey.isEmpty()) {
      uriBuilder.appendQueryParameter("key", mApiKey);
    }
    return uriBuilder;
  }
}
