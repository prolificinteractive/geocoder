package com.prolificinteractive.geocoder;

import com.prolificinteractive.geocoder.model.Address;
import java.util.List;

public interface SwitchPolicy {
  boolean shouldSwitch(final String geocoderName, List<Address> addresses);
}
