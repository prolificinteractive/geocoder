# Geocoder

This is a device independent and plugable replacement for Android's builtin Geocoder.

> A class for handling geocoding and reverse geocoding. Geocoding is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate. Reverse geocoding is the process of transforming a (latitude, longitude) coordinate into a (partial) address. The amount of detail in a reverse geocoded location description may vary, for example one might contain the full street address of the closest building, while another might contain only a city name and postal code.

### Why

Android's implementation of [Geocoder](https://developer.android.com/reference/android/location/Geocoder.html)
requires the a backend service that is not included in AOSP. Geocoder's implementation looks for an on device service that implements the [IGeocodeProvider.aidl](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/location/java/android/location/IGeocodeProvider.aidl) interface. Usually this is implemented by some apk in the system partition. Since this interface is left up to the forker of AOSP to implement, Geocoder can have different implementations across devices and Android versions. Some might not have it implemented at all. Hence why the documentation recommends you call the `isPresent()` method to determine whether a Geocoder implementation exists.

This library was created to provide a consistent experience across all devices and users. The intention here is to provide a library you can drop into you existing application without needing to do much refactoring. Out the box this library provides Geocoding through Google Maps and Open Street Maps. 

### Google Maps 

The Google Maps artifact uses [Google Maps REST API](https://developers.google.com/maps/documentation/geocoding/intro). 
Come June 11th the keyless Google Maps Geocoder in this library with [no longer be supported](https://cloud.google.com/maps-platform/user-guide/) on the new [Google Maps Platform](https://cloud.google.com/maps-platform/). There exists an option to use an API key in the library but it is strongly recommended to not use this is production as you run the risk of leaking the API key. 

Restricting the API key to package name and keystore SHA1 is not an option here as that is not supported by the REST API. Key restricting is only available to users of the Google Play Services SDK, unfortunately Geocoding is not a part of the Google Play Services. Some good news, this was reported to Google way back in 2013 and just recently, this [issue was reopened](https://issuetracker.google.com/issues/35823852) and assigned. Hopefully in the near future we'll have Geocoding support with proper key restriction in Android apps.


### [Open Street Maps](https://wiki.openstreetmap.org/wiki/Nominatim)

The Open Street Maps artifact is subject to Nominations [usage policy](https://operations.osmfoundation.org/policies/nominatim/). Here's an abbreviation: 

 * No heavy uses (an absolute maximum of 1 request per second).
 * Provide a valid HTTP Referer or User-Agent identifying the application (stock User-Agents as set by http libraries will not do).
 * Clearly display attribution as suitable for your medium.
 * Data is provided under the ODbL license which requires to share alike (although small extractions are likely to be covered by fair usage / fair dealing).
 * Apps must make sure that they can switch the service at our request at any time (in particular, switching should be possible without requiring a software update). If at all possible, set up a proxy and also enable caching of requests.

## Installation

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.prolificinteractive.geocoder:geocoder:0.1.0'

    // For Geocoder and Google Maps
    implementation 'com.github.prolificinteractive.geocoder:geocoder-googlemaps:0.1.0'

    // For Geocoder and OkHttp
    implementation 'com.github.prolificinteractive.geocoder:geocoder-okhttp:0.1.0'

    // For Geocoder and Open Street Maps
    implementation 'com.github.prolificinteractive.geocoder:geocoder-openstreetmap:0.1.0'
}
```

### Usage



```
    geocoder = GeocoderBuilder()
        .addGeocodingApi(GoogleMaps.create())
        .setDownloaderFactory(
            OkHttpFactory(OkHttpClient.Builder().build()))
        .build()


    val addresses: List<Address> = geocoder
                                    .getFromLocation("1600 amphitheatre parkway", 1)
                                    .blockingGet()
```

### Roadmap

The pitfalls of the Android Geocoder do not become evident until bad reviews start pouring in.
In order for relieve a pain of having to roll your own solution, this project aim to be a drop in
replacement for the built in Android Geocoder.

- [ ] Remove RxJava from API in favor of Kotlin coroutines.

- [ ] Match method signature of Android's Geocoder.

- [ ] Use Android Platform Address object

- [ ] Streaming JSON parsing



## Contributing to Geocoder

To report a bug or enhancement request, feel free to file an issue under the respective heading.

If you wish  to contribute to the project, fork this repo and submit a pull request. Code contributions should follow the standards specified in the [Prolific Android Style Guide](https://github.com/prolificinteractive/android-code-styles).

## License

![prolific](https://s3.amazonaws.com/prolificsitestaging/logos/Prolific_Logo_Full_Color.png)

geocoder is Copyright (c) 2018 Prolific Interactive. It may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: ./LICENSE
