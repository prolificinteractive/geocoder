package com.prolificinteractive.geocoder.api.openstreetmaps

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.geocoder.Downloader
import com.prolificinteractive.geocoder.GeocodingApi
import com.prolificinteractive.geocoder.model.Address
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.Locale

class OpenStreetMaps private constructor(private val gson: Gson) : GeocodingApi {

  @Throws(Exception::class)
  override fun convert(addresses: List<*>): List<Address> {
    return (addresses as List<OSMAddress>).map { it.getAddress() }
  }

  override fun name() = API_NAME

  @Throws(Exception::class)
  override fun coordinateCall(
      downloader: Downloader,
      latitude: Double,
      longitude: Double): List<OSMAddress> {

    val url = String.format(Locale.getDefault(), OSM_REVERSE_API, latitude, longitude)
    val bytes = downloader.request(url)

    val address = gson.getAdapter(OSMAddress::class.java).fromJson(String(bytes, DEFAULT_CHARSET))
    val addresses = ArrayList<OSMAddress>(1)
    addresses.add(address)
    return addresses
  }

  @Throws(Exception::class)
  override fun locationCall(
      downloader: Downloader,
      locationName: String): List<OSMAddress> {
    val url = String.format(Locale.getDefault(), OSM_GEOCODING_API, locationName)
    val bytes = downloader.request(url)

    val adapter = gson.getAdapter(object : TypeToken<List<OSMAddress>>() {

    })
    return adapter.fromJson(String(bytes, DEFAULT_CHARSET))
  }

  companion object {
    private val DEFAULT_CHARSET = Charset.forName("UTF-8")
    private val API_NAME = "Open Street Maps Api"
    private val OSM_REVERSE_API = "http://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json"
    private val OSM_GEOCODING_API = "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1"

    fun create(gson: Gson): OpenStreetMaps {
      return OpenStreetMaps(gson)
    }
  }
}
