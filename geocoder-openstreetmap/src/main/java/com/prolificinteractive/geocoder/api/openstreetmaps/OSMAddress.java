package com.prolificinteractive.geocoder.api.openstreetmaps;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.geocoder.model.Address;
import com.prolificinteractive.geocoder.model.Location;
import java.util.List;

class OSMAddress {

  @SerializedName("boundingbox")
  @Nullable public List<String> boundingbox;

  @SerializedName("address")
  @Nullable public OSMAddressDetail address;

  @SerializedName("lon")
  @Nullable public String lon;

  @SerializedName("lat")
  @Nullable public String lat;

  @SerializedName("display_name")
  @Nullable public String displayName;

  @SerializedName("type")
  @Nullable public String type;

  @Nullable public Address getAddress() {
    return Address.builder()
        .country(this.address.country)
        .administrativeAreaLevel2(this.address.city)
        .administrativeAreaLevel1(this.address.state)
        .neighborhood(this.address.neighbourhood)
        .postalCode(this.address.postcode)
        .formattedAddress(this.displayName)
        .location(Location.builder()
            .longitude(Double.parseDouble(this.lon))
            .latitude(Double.parseDouble(this.lat))
            .build())
        .build();
  }
}