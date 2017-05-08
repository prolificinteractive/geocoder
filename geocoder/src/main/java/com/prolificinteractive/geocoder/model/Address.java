package com.prolificinteractive.geocoder.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * The Geocoder response value object
 */

@AutoValue
public abstract class Address implements Parcelable {

  @Nullable public abstract String formattedAddress();

  @Nullable public abstract String streetAddress();

  @Nullable public abstract String route();

  @Nullable public abstract String intersection();

  @Nullable public abstract String political();

  @Nullable public abstract String country();

  @Nullable public abstract String administrativeAreaLevel1();

  @Nullable public abstract String administrativeAreaLevel2();

  @Nullable public abstract String administrativeAreaLevel3();

  @Nullable public abstract String administrativeAreaLevel4();

  @Nullable public abstract String administrativeAreaLevel5();

  @Nullable public abstract String colloquialArea();

  @Nullable public abstract String locality();

  @Nullable public abstract String ward();

  @Nullable public abstract String subLocality();

  @Nullable public abstract String subLocalityLevel1();

  @Nullable public abstract String subLocalityLevel2();

  @Nullable public abstract String subLocalityLevel3();

  @Nullable public abstract String subLocalityLevel4();

  @Nullable public abstract String subLocalityLevel5();

  @Nullable public abstract String neighborhood();

  @Nullable public abstract String premise();

  @Nullable public abstract String subPremise();

  @Nullable public abstract String postalCode();

  @Nullable public abstract String naturalFeature();

  @Nullable public abstract String airport();

  @Nullable public abstract String park();

  /*
   * Note: This list is not exhaustive, and is subject to change by Google Geocoding API
   */
  @Nullable public abstract String pointOfInterest();

  @Nullable public abstract String floor();

  @Nullable public abstract String establishment();

  @Nullable public abstract String parking();

  @Nullable public abstract String postBox();

  @Nullable public abstract String postTown();

  @Nullable public abstract String room();

  @Nullable public abstract String streetNumber();

  @Nullable public abstract String busStation();

  @Nullable public abstract String trainStation();

  /*
   * Geometry
   */
  @Nullable public abstract String transitStation();

  @Nullable public abstract Location location();

  @Nullable public abstract String locationType();

  @Nullable public abstract Viewport viewport();

  @Nullable public abstract Bounds bounds();

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_Address.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder formattedAddress(final String formattedAddress);

    public abstract Builder streetAddress(final String streetAddress);

    public abstract Builder route(final String route);

    public abstract Builder intersection(final String intersection);

    public abstract Builder political(final String political);

    public abstract Builder country(final String country);

    public abstract Builder administrativeAreaLevel1(final String administrativeAreaLevel1);

    public abstract Builder administrativeAreaLevel2(final String administrativeAreaLevel2);

    public abstract Builder administrativeAreaLevel3(final String administrativeAreaLevel3);

    public abstract Builder administrativeAreaLevel4(final String administrativeAreaLevel4);

    public abstract Builder administrativeAreaLevel5(final String administrativeAreaLevel5);

    public abstract Builder colloquialArea(final String colloquialArea);

    public abstract Builder locality(final String locality);

    public abstract Builder ward(final String ward);

    public abstract Builder subLocality(final String subLocality);

    public abstract Builder subLocalityLevel1(final String subLocalityLevel1);

    public abstract Builder subLocalityLevel2(final String subLocalityLevel2);

    public abstract Builder subLocalityLevel3(final String subLocalityLevel3);

    public abstract Builder subLocalityLevel4(final String subLocalityLevel4);

    public abstract Builder subLocalityLevel5(final String subLocalityLevel5);

    public abstract Builder neighborhood(final String neighborhood);

    public abstract Builder premise(final String premise);

    public abstract Builder subPremise(final String subPremise);

    public abstract Builder postalCode(final String postalCode);

    public abstract Builder naturalFeature(final String naturalFeature);

    public abstract Builder airport(final String airport);

    public abstract Builder park(final String park);

    public abstract Builder pointOfInterest(final String pointOfInterest);

    public abstract Builder floor(final String floor);

    public abstract Builder establishment(final String establishment);

    public abstract Builder parking(final String parking);

    public abstract Builder postBox(final String postBox);

    public abstract Builder postTown(final String postTown);

    public abstract Builder room(final String room);

    public abstract Builder streetNumber(final String streetNumber);

    public abstract Builder busStation(final String busStation);

    public abstract Builder trainStation(final String trainStation);

    public abstract Builder transitStation(final String transitStation);

    public abstract Builder location(final Location mLocation);

    public abstract Builder locationType(final String locationType);

    public abstract Builder viewport(final Viewport mViewport);

    public abstract Builder bounds(final Bounds mBounds);

    public abstract Address build();
  }
}
