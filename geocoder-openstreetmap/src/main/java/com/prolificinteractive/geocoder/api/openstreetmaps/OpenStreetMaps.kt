package com.prolificinteractive.geocoder.api.openstreetmaps

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.geocoder.Downloader
import com.prolificinteractive.geocoder.GeocodingApi
import com.prolificinteractive.geocoder.model.Address
import java.nio.charset.Charset
import java.util.ArrayList

class OpenStreetMaps private constructor(private val gson: Gson) : GeocodingApi {

  override fun locationCallWithBounds(
      downloader: Downloader,
      locationName: String,
      maxResults: Int,
      lowerLeftLatitude: Double,
      lowerLeftLongitude: Double,
      upperRightLatitude: Double, upperRightLongitude: Double): List<*> {

    val url = buildBaseGeocodingRequestUri()
        .appendQueryParameter("limit", maxResults.toString())
        .appendQueryParameter("q", locationName)
        .appendQueryParameter("viewboxlbrt", String.format(
            "%s,%s,%s,%s",
            lowerLeftLongitude,
            lowerLeftLatitude,
            upperRightLongitude,
            upperRightLatitude
        )).toString()

    val bytes = downloader.request(url)

    val adapter = gson.getAdapter(object : TypeToken<List<OSMAddress>>() {

    })
    return adapter.fromJson(String(bytes, DEFAULT_CHARSET))

  }

  @Throws(Exception::class)
  override fun convert(addresses: List<*>): List<Address> {
    return (addresses as List<OSMAddress>).map { it.getAddress() }
  }

  override fun name() = API_NAME

  @Throws(Exception::class)
  override fun coordinateCall(
      downloader: Downloader,
      latitude: Double,
      longitude: Double, maxResults: Int): List<OSMAddress> {

    val url = buildBaseReverseRequestUri()
        .appendQueryParameter("limit", maxResults.toString())
        .appendQueryParameter("lat", latitude.toString())
        .appendQueryParameter("lon", longitude.toString()).toString()

    val bytes = downloader.request(url)

    val address = gson.getAdapter(OSMAddress::class.java).fromJson(String(bytes, DEFAULT_CHARSET))
    val addresses = ArrayList<OSMAddress>(1)
    addresses.add(address)
    return addresses
  }

  @Throws(Exception::class)
  override fun locationCall(
      downloader: Downloader,
      locationName: String,
      maxResults: Int): List<OSMAddress> {

    val url = buildBaseGeocodingRequestUri()
        .appendQueryParameter("q", locationName)
        .appendQueryParameter("limit", maxResults.toString())
        .toString()

    val bytes = downloader.request(url)

    val adapter = gson.getAdapter(object : TypeToken<List<OSMAddress>>() {

    })
    return adapter.fromJson(String(bytes, DEFAULT_CHARSET))
  }

  private fun buildBaseGeocodingRequestUri(): Uri.Builder {
    return Uri.parse(OSM_GEOCODING_API).buildUpon()
        .appendQueryParameter("format", "json")
        .appendQueryParameter("addressdetails", "1")
  }

  private fun buildBaseReverseRequestUri(): Uri.Builder {
    return Uri.parse(OSM_REVERSE_API).buildUpon()
        .appendQueryParameter("format", "json")
        .appendQueryParameter("addressdetails", "1")
  }

  companion object {
    private val DEFAULT_CHARSET = Charset.forName("UTF-8")
    private val API_NAME = "Open Street Maps Api"
    private val OSM_REVERSE_API = "http://nominatim.openstreetmap.org/reverse"
    private val OSM_GEOCODING_API = "http://nominatim.openstreetmap.org/search"

    fun create(gson: Gson): OpenStreetMaps {
      return OpenStreetMaps(gson)
    }
  }
}
