package com.prolificinteractive.geocoder

import com.prolificinteractive.geocoder.model.Address
import java.util.ArrayList

class GeocoderBuilder {
  private val geocodingApis = ArrayList<GeocodingApi>()
  private var downloaderFactory: Downloader.Factory? = null
  private var switchPolicy: SwitchPolicy = object : SwitchPolicy {
    override fun shouldSwitch(geocoderName: String, addresses: List<Address>): Boolean {
      return false
    }
  }

  fun addGeocodingApi(geocodingApi: GeocodingApi?): GeocoderBuilder {
    if (geocodingApi != null) {
      this.geocodingApis.add(geocodingApi)
    }

    return this
  }

  fun build(): Geocoder {
    return Geocoder(downloaderFactory, switchPolicy, geocodingApis)
  }

  fun setDownloaderFactory(downloaderFactory: Downloader.Factory): GeocoderBuilder {
    this.downloaderFactory = downloaderFactory
    return this
  }

  fun setSwitchPolicy(switchPolicy: SwitchPolicy): GeocoderBuilder {
    this.switchPolicy = switchPolicy
    return this
  }
}
