package com.prolificinteractive.geocoder.api.openstreetmaps;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

class OSMAddressDetail {

  @SerializedName("country")
  @Nullable public String country;

  @SerializedName("country_code")
  @Nullable public String countryCode;

  @SerializedName("city")
  @Nullable public String city;

  @SerializedName("county")
  @Nullable public String county;

  @SerializedName("postcode")
  @Nullable public String postcode;

  @SerializedName("neighbourhood")
  @Nullable public String neighbourhood;

  @SerializedName("suburb")
  @Nullable public String suburb;

  @SerializedName("state")
  @Nullable public String state;
}