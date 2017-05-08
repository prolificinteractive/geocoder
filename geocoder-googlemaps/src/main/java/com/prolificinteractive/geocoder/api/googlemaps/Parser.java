/*
 * Copyright (C) 2015 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prolificinteractive.geocoder.api.googlemaps;

import android.support.annotation.NonNull;
import com.prolificinteractive.geocoder.model.Address;
import com.prolificinteractive.geocoder.model.Bounds;
import com.prolificinteractive.geocoder.model.Location;
import com.prolificinteractive.geocoder.model.Viewport;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parser for Geocoder
 */
public final class Parser {

    /*
     * Status codes which we handle
     */

  private static final String ERROR_MESSAGE = "error_message";

  private static final String STATUS = "status";

  private static final String RESULTS = "results";

  private static final String GEOMETRY = "geometry";

  private static final String LOCATION = "location";

  private static final String LOCATION_TYPE = "location_type";

  private static final String VIEWPORT = "viewport";

  private static final String BOUNDS = "bounds";

  private static final String SOUTHWEST = "southwest";

  private static final String NORTHEAST = "northeast";

  private static final String LAT = "lat";

  private static final String LNG = "lng";

  private static final String FORMATTED_ADDRESS = "formatted_address";

  private static final String ADDRESS_COMPONENTS = "address_components";

  private static final String TYPES = "types";

  private static final String LONG_NAME = "long_name";

  private static final String SHORT_NAME = "short_name";

  private Parser() {
  }

  private static void parseAddressComponents(
      @NonNull final JSONObject result,
      @NonNull final Address.Builder addressBuilder) throws JSONException {
    if (result.has(ADDRESS_COMPONENTS)) {
      final JSONArray addressComponents = result
          .getJSONArray(ADDRESS_COMPONENTS);
      for (int a = 0; a < addressComponents.length(); a++) {
        final JSONObject addressComponent = addressComponents.getJSONObject(a);
        if (!addressComponent.has(TYPES)) {
          continue;
        }
        String value = null;
        if (addressComponent.has(LONG_NAME)) {
          value = addressComponent.getString(LONG_NAME);
        } else if (addressComponent.has(SHORT_NAME)) {
          value = addressComponent.getString(SHORT_NAME);
        }
        if (value == null || value.isEmpty()) {
          continue;
        }
        final JSONArray types = addressComponent.getJSONArray(TYPES);
        for (int t = 0; t < types.length(); t++) {
          final String type = types.getString(t);
          switch (type) {
            case "street_address":
              addressBuilder.streetAddress(value);
              break;

            case "route":
              addressBuilder.route(value);
              break;

            case "intersection":
              addressBuilder.intersection(value);
              break;

            case "political":
              addressBuilder.political(value);
              break;

            case "country":
              addressBuilder.country(value);
              break;

            case "administrative_area_level_1":
              addressBuilder.administrativeAreaLevel1(value);
              break;

            case "administrative_area_level_2":
              addressBuilder.administrativeAreaLevel2(value);
              break;

            case "administrative_area_level_3":
              addressBuilder.administrativeAreaLevel3(value);
              break;

            case "administrative_area_level_4":
              addressBuilder.administrativeAreaLevel4(value);
              break;

            case "administrative_area_level_5":
              addressBuilder.administrativeAreaLevel5(value);
              break;

            case "colloquial_area":
              addressBuilder.colloquialArea(value);
              break;

            case "locality":
              addressBuilder.locality(value);
              break;

            case "ward":
              addressBuilder.ward(value);
              break;

            case "sublocality":
              addressBuilder.subLocality(value);
              break;

            case "sublocality_level_1":
              addressBuilder.subLocalityLevel1(value);
              break;

            case "sublocality_level_2":
              addressBuilder.subLocalityLevel2(value);
              break;

            case "sublocality_level_3":
              addressBuilder.subLocalityLevel3(value);
              break;

            case "sublocality_level_4":
              addressBuilder.subLocalityLevel4(value);
              break;

            case "sublocality_level_5":
              addressBuilder.subLocalityLevel5(value);
              break;

            case "neighborhood":
              addressBuilder.neighborhood(value);

              break;
            case "premise":
              addressBuilder.premise(value);
              break;

            case "subpremise":
              addressBuilder.subPremise(value);
              break;

            case "postal_code":
              addressBuilder.postalCode(value);
              break;

            case "natural_feature":
              addressBuilder.naturalFeature(value);
              break;

            case "airport":
              addressBuilder.airport(value);
              break;

            case "park":
              addressBuilder.park(value);
              break;

            case "point_of_interest":
              addressBuilder.pointOfInterest(value);
              break;

            case "floor":
              addressBuilder.floor(value);
              break;

            case "establishment":
              addressBuilder.establishment(value);
              break;

            case "parking":
              addressBuilder.parking(value);
              break;

            case "post_box":
              addressBuilder.postBox(value);
              break;

            case "postal_town":
              addressBuilder.postTown(value);
              break;

            case "room":
              addressBuilder.room(value);
              break;

            case "street_number":
              addressBuilder.streetNumber(value);
              break;

            case "bus_station":
              addressBuilder.busStation(value);
              break;

            case "train_station":
              addressBuilder.trainStation(value);
              break;

            case "transit_station":
              addressBuilder.transitStation(value);
              break;

            default:
              // Unhandled
              break;
          }
        }
      }
    }
  }

  private static void parseGeometry(
      @NonNull final JSONObject result,
      @NonNull final Address.Builder builder) throws JSONException {
    if (result.has(GEOMETRY)) {
      final JSONObject geometry = result.getJSONObject(GEOMETRY);
      if (geometry.has(LOCATION_TYPE)) {
        builder.locationType(geometry.getString(LOCATION_TYPE));
      }

      if (geometry.has(LOCATION)) {
        final JSONObject location = geometry.getJSONObject(LOCATION);
        builder.location(Location.builder()
            .latitude(location.getDouble(LAT))
            .longitude(location.getDouble(LNG))
            .build());
      }

      if (geometry.has(VIEWPORT)) {
        final JSONObject viewport = geometry.getJSONObject(VIEWPORT);
        if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
          final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
          final Location locationSouthwest = Location.builder()
              .latitude(southwest.getDouble(LAT))
              .longitude(southwest.getDouble(LNG))
              .build();

          final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
          final Location locationNortheast = Location.builder()
              .latitude(northeast.getDouble(LAT))
              .longitude(northeast.getDouble(LNG))
              .build();

          builder.viewport(Viewport.builder()
              .northeast(locationNortheast)
              .southwest(locationSouthwest)
              .build());
        }
      }

      if (geometry.has(BOUNDS)) {
        final JSONObject viewport = geometry.getJSONObject(BOUNDS);
        if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
          final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
          final Location locationSouthwest = Location.builder()
              .latitude(southwest.getDouble(LAT))
              .longitude(southwest.getDouble(LNG))
              .build();

          final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
          final Location locationNortheast = Location.builder()
              .latitude(northeast.getDouble(LAT))
              .longitude(northeast.getDouble(LNG))
              .build();

          builder.bounds(Bounds.builder()
              .southwest(locationSouthwest)
              .northeast(locationNortheast)
              .build());
        }
      }
    }
  }

  @NonNull
  static List<Address> parseJson(
      final byte[] jsonData,
      final int maxResults,
      final boolean parseAddressComponents)
      throws Exception {
    try {
      final String jsonString = new String(jsonData, Charset.forName("UTF-8"));
      final JSONObject jsonObject = new JSONObject(jsonString);

      if (!jsonObject.has(STATUS)) {
        throw new Exception(new JSONException("No \"status\" field"));
      }

      final Status status = Status.fromString(jsonObject.getString(STATUS));
      switch (status) {
        case OK:
          if (jsonObject.has(RESULTS)) {
            return parseResults(maxResults, parseAddressComponents, jsonObject);
          }
          return new ArrayList<>();

        case ZERO_RESULTS:
          return new ArrayList<>();

        default:
          final GoogleMapsException e = GoogleMapsException.forStatus(status);
          try {
            if (jsonObject.has(ERROR_MESSAGE)) {
              e.setErrorMessage(jsonObject.getString(ERROR_MESSAGE));
            }
          } catch (final JSONException ignored) {
          }
          throw e;
      }
    } catch (final JSONException e) {
      throw new GoogleMapsException(e);
    }
  }

  private static List<Address> parseResults(
      final int maxResults,
      final boolean parseAddressComponents,
      @NonNull final JSONObject o) throws JSONException {
    final JSONArray results = o.getJSONArray(RESULTS);
    final int count = results.length() >= maxResults ? maxResults : results.length();
    final ArrayList<Address> addressList = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {

      final Address.Builder addressBuilder = Address.builder();
      final JSONObject result = results.getJSONObject(i);

      if (result.has(FORMATTED_ADDRESS)) {
        addressBuilder.formattedAddress(result.getString(FORMATTED_ADDRESS));
      }

      parseGeometry(result, addressBuilder);

      if (parseAddressComponents) {
        parseAddressComponents(result, addressBuilder);
      }

      addressList.add(addressBuilder.build());
    }
    return addressList;
  }
}
