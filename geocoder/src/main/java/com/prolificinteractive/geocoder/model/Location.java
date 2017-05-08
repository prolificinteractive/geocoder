package com.prolificinteractive.geocoder.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Location implements Parcelable {
  public abstract double latitude();

  public abstract double longitude();

  public static Builder builder() {
    return new AutoValue_Location.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder latitude(final double latitude);

    public abstract Builder longitude(final double longitude);

    public abstract Location build();
  }
}
