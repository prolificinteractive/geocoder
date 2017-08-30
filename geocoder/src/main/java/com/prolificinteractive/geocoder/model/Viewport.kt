package com.prolificinteractive.geocoder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Viewport(
    val southwest: Location,
    val northeast: Location) : Parcelable
