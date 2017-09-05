package com.prolificinteractive.geocoder.api.googlemaps

import android.net.Uri
import com.prolificinteractive.geocoder.Downloader
import com.prolificinteractive.geocoder.GeocodingApi
import com.prolificinteractive.geocoder.RetriableException
import com.prolificinteractive.geocoder.model.Address

class GoogleMaps private constructor(private val mApiKey: String?) : GeocodingApi {

  override fun convert(addresses: List<*>): List<Address> {
    return addresses as List<Address>
  }

  override fun name() = API_NAME

  @Throws(Exception::class)
  override fun coordinateCall(
      downloader: Downloader,
      latitude: Double,
      longitude: Double, maxResults: Int): List<Address> {

    val uriBuilder = buildBaseRequestUri()
        .appendQueryParameter("latlng", latitude.toString() + "," + longitude)

    val data = downloader.request(uriBuilder.toString())

    try {
      return Parser.parseJson(data, MAX_RESULTS, true)
    } catch (e: GoogleMapsException) {
      if (e.status == Status.OVER_QUERY_LIMIT) {
        // OVER_QUERY_LIMIT is is an error that is eligible for retrying.
        throw RetriableException(e.toString())
      } else {
        throw e
      }
    }

  }

  override fun locationCallWithBounds(
      downloader: Downloader,
      locationName: String,
      maxResults: Int,
      lowerLeftLatitude: Double,
      lowerLeftLongitude: Double,
      upperRightLatitude: Double, upperRightLongitude: Double): List<*> {

    val uriBuilder = buildBaseRequestUri()
        .appendQueryParameter("address", locationName)
        .appendQueryParameter("bounds" , String.format(
            "%s,%s|%s,%s",
            lowerLeftLatitude,
            lowerLeftLongitude,
            upperRightLatitude,
            upperRightLongitude)
        )

    val url = uriBuilder.toString()

    val data = downloader.request(url)

    try {
      return Parser.parseJson(data, MAX_RESULTS, true)
    } catch (e: GoogleMapsException) {
      if (e.status == Status.OVER_QUERY_LIMIT) {
        throw RetriableException(e.toString())
      } else {
        throw e }
    }

  }

  @Throws(Exception::class)
  override fun locationCall(
      downloader: Downloader,
      locationName: String, maxResults: Int): List<Address> {

    val uriBuilder = buildBaseRequestUri()
        .appendQueryParameter("address", locationName)

    val url = uriBuilder.toString()

    val data = downloader.request(url)

    try {
      return Parser.parseJson(data, MAX_RESULTS, true)
    } catch (e: GoogleMapsException) {
      if (e.status == Status.OVER_QUERY_LIMIT) {
        throw RetriableException(e.toString())
      } else {
        throw e
      }
    }

  }

  private fun buildBaseRequestUri(): Uri.Builder {
    val uriBuilder = Uri.parse(ENDPOINT_URL).buildUpon()
    if (mApiKey != null && !mApiKey.isEmpty()) {
      uriBuilder.appendQueryParameter("key", mApiKey)
    }
    return uriBuilder
  }

  companion object {
    private val API_NAME = "Google Maps Api"
    private val ENDPOINT_URL = "https://maps.googleapis.com/maps/api/geocode/json"
    private val MAX_RESULTS = -1

    @JvmStatic
    fun create(): GoogleMaps {
      return GoogleMaps("")
    }

    @JvmStatic
    fun create(mApiKey: String): GoogleMaps {
      return GoogleMaps(mApiKey)
    }
  }
}
