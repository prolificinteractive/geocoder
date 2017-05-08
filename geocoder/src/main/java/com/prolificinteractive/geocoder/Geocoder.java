package com.prolificinteractive.geocoder;

import android.support.annotation.NonNull;
import com.prolificinteractive.geocoder.model.Address;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;

public class Geocoder {
  private static final int RETRY_DELAY_MILLIS = 2000;
  private static final int RETRY_MAX = 2;

  private static final double LAT_MIN = -90.0;
  private static final double LAT_MAX = 90.0;
  private static final double LONG_MIN = -180.0;
  private static final double LONG_MAX = 180.0;

  private final Downloader.Factory downloaderFactory;
  private final List<? extends GeocodingApi> geocodingApis;
  private final SwitchPolicy switchPolicy;

  Geocoder(
      final Downloader.Factory downloaderFactory,
      final SwitchPolicy switchPolicy,
      final List<? extends GeocodingApi> geocodingApis) {
    this.downloaderFactory = downloaderFactory;
    this.geocodingApis = geocodingApis;
    this.switchPolicy = switchPolicy;
  }

  /**
   * Returns an array of Addresses that are known to describe the named
   * location, which may be a place name such as "Dalvik,
   * Iceland", an address such as "1600 Amphitheatre Parkway, Mountain View,
   * CA", an airport code such as "SFO", etc.. The returned addresses will be
   * localized for the locale provided to this class's constructor.
   *
   * <p>
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
   *                                  is unavailable or any other I/O problem occurs
   */
  public Single<List<Address>> getFromLocation(final String locationName) {
    final Downloader downloader = downloaderFactory.create();
    return Single.create(new SingleOnSubscribe<List<Address>>() {
      @Override public void subscribe(final SingleEmitter<List<Address>> e) throws Exception {
        e.setDisposable(downloader.getDisposable());
        try {
          final List<Address> addresses = getFromLocationName(downloader, locationName);
          e.onSuccess(addresses);
        } catch (final Throwable t) {
          if (!e.isDisposed()) {
            e.onError(t);
          }
        }
      }
    }).retryWhen(new RetryWithDelay(RETRY_MAX, RETRY_DELAY_MILLIS));
  }

  /**
   * Returns an array of Addresses that are known to describe the area
   * immediately surrounding the given latitude and longitude. The returned
   * addresses will be localized for the locale provided to this class's
   * constructor.
   *
   * <p>
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
  public Single<List<Address>> getFromLocation(final double latitude, final double longitude) {
    final Downloader downloader = downloaderFactory.create();
    return Single.create(new SingleOnSubscribe<List<Address>>() {
      @Override public void subscribe(final SingleEmitter<List<Address>> e) throws Exception {
        e.setDisposable(downloader.getDisposable());
        try {
          final List<Address> addresses = getFromLocation(downloader, latitude, longitude);
          e.onSuccess(addresses);
        } catch (final Throwable t) {
          if (!e.isDisposed()) {
            e.onError(t);
          }
        }
      }
    }).retryWhen(new RetryWithDelay(RETRY_MAX, RETRY_DELAY_MILLIS));
  }

  @NonNull private List<Address> getFromLocation(
      final Downloader downloader,
      final double latitude,
      final double longitude)
      throws Exception {

    if (latitude < LAT_MIN || latitude > LAT_MAX) {
      throw new IllegalArgumentException("latitude == " + latitude);
    }
    if (longitude < LONG_MIN || longitude > LONG_MAX) {
      throw new IllegalArgumentException("longitude == " + longitude);
    }

    List<Address> newAddress = new ArrayList<>();
    for (final GeocodingApi geocodingApi : geocodingApis) {
      final List<?> address = geocodingApi.coordinateCall(downloader, latitude, longitude);
      newAddress = geocodingApi.convert(address);

      if (!switchPolicy.shouldSwitch(geocodingApi.getName(), newAddress)) {
        break;
      }
    }

    return newAddress;
  }

  @NonNull private List<Address> getFromLocationName(
      final Downloader downloader,
      final String locationName)
      throws Exception {
    if (locationName == null) {
      throw new IllegalArgumentException("locationName == null");
    }
    List<Address> newAddress = new ArrayList<>();
    for (final GeocodingApi geocodingApi : geocodingApis) {
      final List<?> address = geocodingApi.locationCall(downloader, locationName);
      newAddress = geocodingApi.convert(address);
      if (!switchPolicy.shouldSwitch(geocodingApi.getName(), newAddress)) {
        break;
      }
    }

    return newAddress;
  }

  /**
   * The class manages retrying when a {@link RetriableException} propagated through the Rx chain.
   */
  private class RetryWithDelay implements Function<Flowable<? extends Throwable>, Publisher<Object>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
      this.maxRetries = maxRetries;
      this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Publisher<Object> apply(final Flowable<? extends Throwable> inputObservable) {

      // it is critical to use inputObservable in the chain for the result
      // ignoring it and doing your own thing will break the sequence
      return inputObservable.flatMap(new Function<Throwable, Publisher<?>>() {
        @Override
        public Publisher<?> apply(final Throwable throwable) {
          if (throwable instanceof RetriableException) {
            if (++retryCount < maxRetries) {

              // When this Observable calls onNext, the original
              // Observable will be retried (i.e. re-subscribed)
              return Flowable.timer(retryCount * retryDelayMillis, TimeUnit.MILLISECONDS);
            } else {
              return Flowable.error(throwable.getCause());
            }
          }

          // Max retries hit or throwable is not a tryable exception Pass an error so the chain is forcibly completed
          // only onNext triggers a re-subscription (onError + onComplete kills it)
          return Flowable.error(throwable);
        }
      });
    }
  }
}
