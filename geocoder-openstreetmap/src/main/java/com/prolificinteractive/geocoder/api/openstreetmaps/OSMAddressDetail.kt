package com.prolificinteractive.geocoder.api.openstreetmaps

import com.google.gson.annotations.SerializedName

data class OSMAddressDetail(

  @SerializedName("country")
  var country: String? = null,

  @SerializedName("country_code")
  var countryCode: String? = null,

  @SerializedName("city")
  var city: String? = null,

  @SerializedName("county")
  var county: String? = null,

  @SerializedName("postcode")
  var postcode: String? = null,

  @SerializedName("neighbourhood")
  var neighbourhood: String? = null,

  @SerializedName("suburb")
  var suburb: String? = null,

  @SerializedName("state")
  var state: String? = null
)