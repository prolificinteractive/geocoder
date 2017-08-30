package com.prolificinteractive.geocoder

import com.prolificinteractive.geocoder.model.Address
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import org.reactivestreams.Publisher

class Geocoder internal constructor(
    private val downloaderFactory: Downloader.Factory?,
    private val switchPolicy: SwitchPolicy?,
    private val geocodingApis: List<GeocodingApi>) {

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
   * @param locationName           a user-supplied description of a location
   *
   * @return a list of Address objects. Returns empty list if no matches were found.
   * @throws IllegalArgumentException if locationName is null
   * @throws Exception        if parse failed, or if the network
   * is unavailable or any other I/O problem occurs
   */
  fun getFromLocation(locationName: String): Single<List<Address>> {
    val downloader = downloaderFactory!!.create()
    return Single.create(SingleOnSubscribe<List<Address>> { e ->
      e.setDisposable(downloader.disposable)
      try {
        val addresses = getFromLocationName(downloader, locationName)
        e.onSuccess(addresses)
      } catch (t: Throwable) {
        if (!e.isDisposed) {
          e.onError(t)
        }
      }
    }).retryWhen(RetryWithDelay(RETRY_MAX, RETRY_DELAY_MILLIS))
  }

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
   * @param latitude               the latitude a point for the search
   * @param longitude              the longitude a point for the search
   *
   * @return Single list of Address objects. Returns empty list if no matches were found.
   * @throws IllegalArgumentException if latitude is less than -90 or greater than 90
   * @throws IllegalArgumentException if longitude is less than -180 or greater than 180
   * @throws Exception  If the network is unavailable or any other I/O problem occurs
   */
  fun getFromLocation(latitude: Double, longitude: Double): Single<List<Address>> {
    val downloader = downloaderFactory!!.create()
    return Single.create(SingleOnSubscribe<List<Address>> { e ->
      e.setDisposable(downloader.disposable)
      try {
        val addresses = getFromLocation(downloader, latitude, longitude)
        e.onSuccess(addresses)
      } catch (t: Throwable) {
        if (!e.isDisposed) {
          e.onError(t)
        }
      }
    }).retryWhen(RetryWithDelay(RETRY_MAX, RETRY_DELAY_MILLIS))
  }

  @Throws(Exception::class)
  private fun getFromLocation(
      downloader: Downloader,
      latitude: Double,
      longitude: Double): List<Address> {

    if (latitude < LAT_MIN || latitude > LAT_MAX) {
      throw IllegalArgumentException("latitude == " + latitude)
    }
    if (longitude < LONG_MIN || longitude > LONG_MAX) {
      throw IllegalArgumentException("longitude == " + longitude)
    }

    var newAddress: List<Address> = ArrayList()
    for (geocodingApi in geocodingApis) {
      val address = geocodingApi.coordinateCall(downloader, latitude, longitude)
      newAddress = geocodingApi.convert(address)

      if (!switchPolicy!!.shouldSwitch(geocodingApi.name(), newAddress)) {
        break
      }
    }

    return newAddress
  }

  @Throws(Exception::class)
  private fun getFromLocationName(
      downloader: Downloader,
      locationName: String?): List<Address> {
    if (locationName == null) {
      throw IllegalArgumentException("locationName == null")
    }
    var newAddress: List<Address> = ArrayList()
    for (geocodingApi in geocodingApis) {
      val address = geocodingApi.locationCall(downloader, locationName)
      newAddress = geocodingApi.convert(address)
      if (!switchPolicy!!.shouldSwitch(geocodingApi.name(), newAddress)) {
        break
      }
    }

    return newAddress
  }

  /**
   * The class manages retrying when a [RetriableException] propagated through the Rx chain.
   */
  private inner class RetryWithDelay(private val maxRetries: Int, private val retryDelayMillis: Int) : Function<Flowable<out Throwable>, Publisher<Any>> {
    private var retryCount: Int = 0

    override fun apply(inputObservable: Flowable<out Throwable>): Publisher<Any> {

      // it is critical to use inputObservable in the chain for the result
      // ignoring it and doing your own thing will break the sequence
      return inputObservable.flatMap(Function { throwable ->
        if (throwable is RetriableException) {
          return@Function if (++retryCount < maxRetries) {

            // When this Observable calls onNext, the original
            // Observable will be retried (i.e. re-subscribed)
            Flowable.timer((retryCount * retryDelayMillis).toLong(), TimeUnit.MILLISECONDS)
          } else {
            Flowable.error<Any>(throwable.cause)
          }
        }

        // Max retries hit or throwable is not a tryable exception Pass an error so the chain is forcibly completed
        // only onNext triggers a re-subscription (onError + onComplete kills it)
        Flowable.error(throwable)
      })
    }
  }

  companion object {
    private val RETRY_DELAY_MILLIS = 2000
    private val RETRY_MAX = 2

    private val LAT_MIN = -90.0
    private val LAT_MAX = 90.0
    private val LONG_MIN = -180.0
    private val LONG_MAX = 180.0
  }
}
