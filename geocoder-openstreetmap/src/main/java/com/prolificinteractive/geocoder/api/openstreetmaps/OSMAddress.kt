package com.prolificinteractive.geocoder.api.openstreetmaps

import com.google.gson.annotations.SerializedName
import com.prolificinteractive.geocoder.model.Address
import com.prolificinteractive.geocoder.model.Location

data class OSMAddress(

    @SerializedName("boundingbox")
    val boundingbox: List<String>? = null,

    @SerializedName("address")
    val osmAddress: OSMAddressDetail? = null,

    @SerializedName("lon")
    val lon: String? = null,

    @SerializedName("lat")
    val lat: String? = null,

    @SerializedName("display_name")
    val displayName: String? = null,

    @SerializedName("type")
    val type: String? = null
) {
  fun getAddress(): Address {
    return Address(
        country = this.osmAddress?.country,
        administrativeAreaLevel2 = this.osmAddress?.city,
        administrativeAreaLevel1 = this.osmAddress?.state,
        neighborhood = this.osmAddress?.neighbourhood,
        postalCode = this.osmAddress?.postcode,
        formattedAddress = this.displayName,
        location = Location(
            longitude = this.lon?.toDouble(),
            latitude = this.lat?.toDouble()
        )
    )
  }
}