package com.prolificinteractive.geocoder.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Viewport implements Parcelable {

  public abstract Location southwest();

  public abstract Location northeast();

  public static Builder builder() {
    return new AutoValue_Viewport.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder southwest(final Location southwest);

    public abstract Builder northeast(final Location northeast);

    public abstract Viewport build();
  }
}