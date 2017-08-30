package com.prolificinteractive.geocoder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val latitude: Double?,
    val longitude: Double?) : Parcelable
