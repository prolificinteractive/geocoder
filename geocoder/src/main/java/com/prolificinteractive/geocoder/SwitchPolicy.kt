package com.prolificinteractive.geocoder

import com.prolificinteractive.geocoder.model.Address


@FunctionalInterface
interface SwitchPolicy {
  fun shouldSwitch(geocoderName: String, addresses: List<Address>): Boolean
}
