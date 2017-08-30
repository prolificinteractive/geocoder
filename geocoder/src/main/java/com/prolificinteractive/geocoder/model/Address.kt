package com.prolificinteractive.geocoder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * The Geocoder response value object
 */

@Parcelize
data class Address(
    val formattedAddress: String? = null,
    val streetAddress: String? = null,
    val route: String? = null,
    val intersection: String? = null,
    val political: String? = null,
    val country: String? = null,
    val administrativeAreaLevel1: String? = null,
    val administrativeAreaLevel2: String? = null,
    val administrativeAreaLevel3: String? = null,
    val administrativeAreaLevel4: String? = null,
    val administrativeAreaLevel5: String? = null,
    val colloquialArea: String? = null,
    val locality: String? = null,
    val ward: String? = null,
    val subLocality: String? = null,
    val subLocalityLevel1: String? = null,
    val subLocalityLevel2: String? = null,
    val subLocalityLevel3: String? = null,
    val subLocalityLevel4: String? = null,
    val subLocalityLevel5: String? = null,
    val neighborhood: String? = null,
    val premise: String? = null,
    val subPremise: String? = null,
    val postalCode: String? = null,
    val naturalFeature: String? = null,
    val airport: String? = null,
    val park: String? = null,

    /*
     * Note: This list is not exhaustive, and is subject to change by Google Geocoding API
     */
    val pointOfInterest: String? = null,
    val floor: String? = null,
    val establishment: String? = null,
    val parking: String? = null,
    val postBox: String? = null,
    val postTown: String? = null,
    val room: String? = null,
    val streetNumber: String? = null,
    val busStation: String? = null,
    val trainStation: String? = null,

    /*
     * Geometry
     */
    val transitStation: String? = null,
    val location: Location? = null,
    val locationType: String? = null,
    val viewport: Viewport? = null,
    val bounds: Bounds? = null
) : Parcelable