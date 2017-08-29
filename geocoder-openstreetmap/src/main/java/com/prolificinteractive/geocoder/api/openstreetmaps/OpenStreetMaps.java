package com.prolificinteractive.geocoder.api.openstreetmaps;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.geocoder.Downloader;
import com.prolificinteractive.geocoder.GeocodingApi;
import com.prolificinteractive.geocoder.model.Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("DM_DEFAULT_ENCODING")
public final class OpenStreetMaps implements GeocodingApi {
  private static final String DEFAULT_CHARSET = "UTF-8";
  private static final String API_NAME = "Open Street Maps Api";
  private static final String OSM_REVERSE_API =
      "http://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json";
  private static final String OSM_GEOCODING_API =
      "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1";
  private final Gson gson;

  private OpenStreetMaps(final Gson gson) {
    this.gson = gson;
  }

  public static OpenStreetMaps create(final Gson gson) {
    return new OpenStreetMaps(gson);
  }

  @Override public List<Address> convert(final List<?> addresses) throws Exception {
    final List<Address> newAddresses = new ArrayList<>();
    for (final OSMAddress address : (List<OSMAddress>) addresses) {
      newAddresses.add(address.getAddress());
    }
    return newAddresses;
  }

  @Override public List<OSMAddress> coordinateCall(
      final Downloader downloader,
      final double latitude,
      final double longitude)
      throws Exception {

    final String url = String.format(Locale.getDefault(), OSM_REVERSE_API, latitude, longitude);
    final byte[] bytes = downloader.request(url);

    final OSMAddress address =
        gson.getAdapter(OSMAddress.class).fromJson(new String(bytes, DEFAULT_CHARSET));
    final List<OSMAddress> addresses = new ArrayList<>(1);
    addresses.add(address);
    return addresses;
  }

  @Override public String getName() {
    return API_NAME;
  }

  @Override public List<OSMAddress> locationCall(
      final Downloader downloader,
      final String locationName)
      throws Exception {
    final String url = String.format(Locale.getDefault(), OSM_GEOCODING_API, locationName);
    final byte[] bytes = downloader.request(url);

    final TypeAdapter<List<OSMAddress>> adapter =
        gson.getAdapter(new TypeToken<List<OSMAddress>>() { });
    return adapter.fromJson(new String(bytes, DEFAULT_CHARSET));
  }
}
