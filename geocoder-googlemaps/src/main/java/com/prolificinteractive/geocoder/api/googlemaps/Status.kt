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

/**
 * Geocoder status
 *
 * https://developers.google.com/maps/documentation/geocoding/intro#StatusCodes
 */
enum class Status {

  /**
   * Indicates that no errors occurred; the address was successfully parsed and at least one
   * geocode was returned.
   */
  OK,

  /**
   * Indicates that the geocode was successful but returned no results. This may occur if the
   * geocoder was passed a non-existent address
   */
  ZERO_RESULTS,

  /**
   * Indicates that you are over your quota.
   */
  OVER_QUERY_LIMIT,

  /**
   * Indicates that your request was denied
   */
  REQUEST_DENIED,

  /**
   * Generally indicates that the query (address, components or latlng) is missing.
   */
  INVALID_REQUEST,

  /**
   * Indicates that the request could not be processed due to a server error. The request may
   * succeed if you try again.
   */
  UNKNOWN_ERROR;


  companion object {
    internal fun fromString(status: String?): Status {
      if (status == null || status.isEmpty()) {
        return UNKNOWN_ERROR
      }
      return try {
        valueOf(status)
      } catch (e: IllegalArgumentException) {
        UNKNOWN_ERROR
      }

    }
  }
}
