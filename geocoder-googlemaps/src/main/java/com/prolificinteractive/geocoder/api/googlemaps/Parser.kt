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

package com.prolificinteractive.geocoder.api.googlemaps

import com.prolificinteractive.geocoder.model.Address
import com.prolificinteractive.geocoder.model.Bounds
import com.prolificinteractive.geocoder.model.Location
import com.prolificinteractive.geocoder.model.Viewport
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Parser for Geocoder
 */
class Parser {

  companion object {

    /*
   * Status codes which we handle
   */

    private val ERROR_MESSAGE = "error_message"

    private val STATUS = "status"

    private val RESULTS = "results"

    private val GEOMETRY = "geometry"

    private val LOCATION = "location"

    private val LOCATION_TYPE = "location_type"

    private val VIEWPORT = "viewport"

    private val BOUNDS = "bounds"

    private val SOUTHWEST = "southwest"

    private val NORTHEAST = "northeast"

    private val LAT = "lat"

    private val LNG = "lng"

    private val FORMATTED_ADDRESS = "formatted_address"

    private val ADDRESS_COMPONENTS = "address_components"

    private val TYPES = "types"

    private val LONG_NAME = "long_name"

    private val SHORT_NAME = "short_name"


    @Throws(JSONException::class)
    private fun parseAddressComponents(
        result: JSONObject,
        address: Address): Address {

      var formattedAddress: String? = null
      var streetAddress: String? = null
      var route: String? = null
      var intersection: String? = null
      var political: String? = null
      var country: String? = null
      var administrativeAreaLevel1: String? = null
      var administrativeAreaLevel2: String? = null
      var administrativeAreaLevel3: String? = null
      var administrativeAreaLevel4: String? = null
      var administrativeAreaLevel5: String? = null
      var colloquialArea: String? = null
      var locality: String? = null
      var ward: String? = null
      var subLocality: String? = null
      var subLocalityLevel1: String? = null
      var subLocalityLevel2: String? = null
      var subLocalityLevel3: String? = null
      var subLocalityLevel4: String? = null
      var subLocalityLevel5: String? = null
      var neighborhood: String? = null
      var premise: String? = null
      var subPremise: String? = null
      var postalCode: String? = null
      var naturalFeature: String? = null
      var airport: String? = null
      var park: String? = null
      var pointOfInterest: String? = null
      var floor: String? = null
      var establishment: String? = null
      var parking: String? = null
      var postBox: String? = null
      var postTown: String? = null
      var room: String? = null
      var streetNumber: String? = null
      var busStation: String? = null
      var trainStation: String? = null
      var transitStation: String? = null

      if (result.has(ADDRESS_COMPONENTS)) {
        val addressComponents = result
            .getJSONArray(ADDRESS_COMPONENTS)
        for (a in 0 until addressComponents.length()) {
          val addressComponent = addressComponents.getJSONObject(a)
          if (!addressComponent.has(TYPES)) {
            continue
          }
          var value: String? = null
          if (addressComponent.has(LONG_NAME)) {
            value = addressComponent.getString(LONG_NAME)
          } else if (addressComponent.has(SHORT_NAME)) {
            value = addressComponent.getString(SHORT_NAME)
          }
          if (value == null || value.isEmpty()) {
            continue
          }
          val types = addressComponent.getJSONArray(TYPES)
          for (t in 0 until types.length()) {
            val type = types.getString(t)
            when (type) {
              "street_address" -> streetAddress = value
              "route" -> route = value
              "intersection" -> intersection = value
              "political" -> political = value
              "country" -> country = value
              "administrative_area_level_1" -> administrativeAreaLevel1 = value
              "administrative_area_level_2" -> administrativeAreaLevel2 = value
              "administrative_area_level_3" -> administrativeAreaLevel3 = value
              "administrative_area_level_4" -> administrativeAreaLevel4 = value
              "administrative_area_level_5" -> administrativeAreaLevel5 = value
              "colloquial_area" -> colloquialArea = value
              "locality" -> locality = value
              "ward" -> ward = value
              "sublocality" -> subLocality = value
              "sublocality_level_1" -> subLocalityLevel1 = value
              "sublocality_level_2" -> subLocalityLevel2 = value
              "sublocality_level_3" -> subLocalityLevel3 = value
              "sublocality_level_4" -> subLocalityLevel4 = value
              "sublocality_level_5" -> subLocalityLevel5 = value
              "neighborhood" -> neighborhood = value
              "premise" -> premise = value
              "subpremise" -> subPremise = value
              "postal_code" -> postalCode = value
              "natural_feature" -> naturalFeature = value
              "airport" -> airport = value
              "park" -> park = value
              "point_of_interest" -> pointOfInterest = value
              "floor" -> floor = value
              "establishment" -> establishment = value
              "parking" -> parking = value
              "post_box" -> postBox = value
              "postal_town" -> postTown = value
              "room" -> room = value
              "street_number" -> streetNumber = value
              "bus_station" -> busStation = value
              "train_station" -> trainStation = value
              "transit_station" -> transitStation = value
            }
          }
        }
      }

      return address.copy(formattedAddress = formattedAddress,
          streetAddress = streetAddress,
          route = route,
          intersection = intersection,
          political = political,
          country = country,
          administrativeAreaLevel1 = administrativeAreaLevel1,
          administrativeAreaLevel2 = administrativeAreaLevel2,
          administrativeAreaLevel3 = administrativeAreaLevel3,
          administrativeAreaLevel4 = administrativeAreaLevel4,
          administrativeAreaLevel5 = administrativeAreaLevel5,
          colloquialArea = colloquialArea,
          locality = locality,
          ward = ward,
          subLocality = subLocality,
          subLocalityLevel1 = subLocalityLevel1,
          subLocalityLevel2 = subLocalityLevel2,
          subLocalityLevel3 = subLocalityLevel3,
          subLocalityLevel4 = subLocalityLevel4,
          subLocalityLevel5 = subLocalityLevel5,
          neighborhood = neighborhood,
          premise = premise,
          subPremise = subPremise,
          postalCode = postalCode,
          naturalFeature = naturalFeature,
          airport = airport,
          park = park,
          pointOfInterest = pointOfInterest,
          floor = floor,
          establishment = establishment,
          parking = parking,
          postBox = postBox,
          postTown = postTown,
          room = room,
          streetNumber = streetNumber,
          busStation = busStation,
          trainStation = trainStation,
          transitStation = transitStation)
    }

    private fun parseGeometry(
        result: JSONObject,
        address: Address): Address {

      var locationType: String? = null
      var location: Location? = null
      var viewport: Viewport? = null
      var bounds: Bounds? = null

      if (result.has(GEOMETRY)) {
        val geometry = result.getJSONObject(GEOMETRY)
        if (geometry.has(LOCATION_TYPE)) {
          locationType = geometry.getString(LOCATION_TYPE)
        }

        if (geometry.has(LOCATION)) {
          val locationJson = geometry.getJSONObject(LOCATION)
          location = Location(locationJson.getDouble(LAT), locationJson.getDouble(LNG))
        }

        if (geometry.has(VIEWPORT)) {
          val viewportJson = geometry.getJSONObject(VIEWPORT)
          if (viewportJson.has(SOUTHWEST) && viewportJson.has(NORTHEAST)) {
            val southwest = viewportJson.getJSONObject(SOUTHWEST)
            val locationSouthwest = Location(southwest.getDouble(LAT), southwest.getDouble(LNG))

            val northeast = viewportJson.getJSONObject(NORTHEAST)
            val locationNortheast = Location(northeast.getDouble(LAT), northeast.getDouble(LNG))

            viewport = Viewport(locationNortheast, locationSouthwest)
          }
        }

        if (geometry.has(BOUNDS)) {
          val viewportJson = geometry.getJSONObject(BOUNDS)
          if (viewportJson.has(SOUTHWEST) && viewportJson.has(NORTHEAST)) {
            val southwest = viewportJson.getJSONObject(SOUTHWEST)
            val locationSouthwest = Location(southwest.getDouble(LAT), southwest.getDouble(LNG))

            val northeast = viewportJson.getJSONObject(NORTHEAST)
            val locationNortheast = Location(northeast.getDouble(LAT) ,(northeast.getDouble(LNG)))

            bounds = Bounds(locationSouthwest, locationNortheast)
          }
        }
      }

      return address.copy(
          locationType = locationType,
          location = location,
          viewport = viewport,
          bounds = bounds
      )
    }

    @Throws(GoogleMapsException::class)
    fun parseJson(
        jsonData: ByteArray,
        maxResults: Int,
        parseAddressComponents: Boolean): List<Address> {
      try {
        val jsonString = String(jsonData, Charset.forName("UTF-8"))
        val jsonObject = JSONObject(jsonString)

        if (!jsonObject.has(STATUS)) {
          throw Exception(JSONException("No \"status\" field"))
        }

        val status = Status.fromString(jsonObject.getString(STATUS))
        when (status) {
          Status.OK -> {
            return if (jsonObject.has(RESULTS)) {
              parseResults(maxResults, parseAddressComponents, jsonObject)
            } else ArrayList()
          }

          Status.ZERO_RESULTS -> return ArrayList()

          else -> {
            val e = GoogleMapsException.forStatus(status)
            try {
              if (jsonObject.has(ERROR_MESSAGE)) {
                e.errorMessage = jsonObject.getString(ERROR_MESSAGE)
              }
            } catch (ignored: JSONException) {
            }

            throw e
          }
        }
      } catch (e: JSONException) {
        throw GoogleMapsException(e)
      }

    }

    @Throws(JSONException::class)
    fun parseResults(
        maxResults: Int,
        parseAddressComponents: Boolean,
        jsonObject: JSONObject): List<Address> {
      val results = jsonObject.getJSONArray(RESULTS)
      val count = if (results.length() >= maxResults && maxResults != -1) maxResults else results.length()
      val addressList = ArrayList<Address>(count)
      for (i in 0 until count) {

        var address = Address()
        val result = results.getJSONObject(i)

        if (result.has(FORMATTED_ADDRESS)) {
          address = address.copy(formattedAddress = result.getString(FORMATTED_ADDRESS))
        }

        address = parseGeometry(result, address)

        if (parseAddressComponents) {
          address = parseAddressComponents(result, address)
        }

        addressList.add(address)
      }
      return addressList
    }
  }

}
