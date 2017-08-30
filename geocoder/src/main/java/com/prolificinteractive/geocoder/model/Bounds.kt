package com.prolificinteractive.geocoder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bounds(
    val southwest: Location,
    val northeast: Location) : Parcelable