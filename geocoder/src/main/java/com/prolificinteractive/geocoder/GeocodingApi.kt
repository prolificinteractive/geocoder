package com.prolificinteractive.geocoder

import com.prolificinteractive.geocoder.model.Address

interface GeocodingApi {

  /**
   * Provide the name of the API.
   *
   * @return the api name.
   */
  fun name(): String

  /**
   * Returns an array of Addresses that are known to describe the named
   * location, which may be a place name such as "Dalvik,
   * Iceland", an address such as "1600 Amphitheatre Parkway, Mountain View,
   * CA", an airport code such as "SFO", etc.. The returned addresses will be
   * localized for the locale provided to this class's constructor.
   *
   *
   *
   * The query will block and returned values will be obtained by means of a
   * network lookup. The results are a best guess and are not guaranteed to be
   * meaningful or correct. It may be useful to call this method from a thread
   * separate from your primary UI thread.
   *
   * @param downloader             the interface to perform downloads
   * @param locationName           a user-supplied description of a location
   *
   * @return a list of Address objects. Returns empty list if no matches were found.
   * @throws Exception        if parse failed, or if the network
   * is unavailable or any other I/O problem occurs
   */
  @Throws(Exception::class)
  fun locationCall(downloader: Downloader, locationName: String, maxResults: Int): List<*>

  /**
   * Returns an array of Addresses that are known to describe the named
   * location, which may be a place name such as "Dalvik,
   * Iceland", an address such as "1600 Amphitheatre Parkway, Mountain View,
   * CA", an airport code such as "SFO", etc.. The returned addresses will be
   * localized for the locale provided to this class's constructor.
   *
   *
   *
   * The query will block and returned values will be obtained by means of a
   * network lookup. The results are a best guess and are not guaranteed to be
   * meaningful or correct. It may be useful to call this method from a thread
   * separate from your primary UI thread.
   *
   * @param downloader             the interface to perform downloads
   * @param locationName           a user-supplied description of a location
   * @param lowerLeftLatitude      the latitude of the southwest point
   * @param lowerLeftLongitude     the longitude of the southwest point
   * @param upperRightLatitude     the latitude of the northeast point
   * @param upperRightLongitude    the longitude of the northeast point
   *
   * @return a list of Address objects. Returns empty list if no matches were found.
   * @throws Exception        if parse failed, or if the network
   * is unavailable or any other I/O problem occurs
   */
  @Throws(Exception::class)
  fun locationCallWithBounds(downloader: Downloader,
                             locationName: String,
                             maxResults: Int,
                             lowerLeftLatitude: Double,
                             lowerLeftLongitude: Double,
                             upperRightLatitude: Double,
                             upperRightLongitude: Double): List<*>


  /**
   * Returns an array of Addresses that are known to describe the area
   * immediately surrounding the given latitude and longitude. The returned
   * addresses will be localized for the locale provided to this class's
   * constructor.
   *
   *
   *
   * The returned values may be obtained by means of a network lookup. The
   * results are a best guess and are not guaranteed to be meaningful or
   * correct. It may be useful to call this method from a thread separate from
   * your primary UI thread.
   *
   * @param downloader             the interface to perform downloads
   * @param latitude               the latitude a point for the search
   * @param longitude              the longitude a point for the search
   *
   * @return a list of Address objects. Returns empty list if no matches were found.
   * @throws Exception  If the network is unavailable or any other I/O problem occurs
   */
  @Throws(Exception::class)
  fun coordinateCall(
      downloader: Downloader,
      latitude: Double,
      longitude: Double,
      maxResults: Int): List<*>

  /**
   * Performs a conversion from the provided APIs address object to a list of [Address].
   *
   * @return a list of [Address] objects.
   */
  @Throws(Exception::class)
  fun convert(addresses: List<*>): List<Address>
}
