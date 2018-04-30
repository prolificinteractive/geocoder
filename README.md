### Geocoder

This is a device independent and plugable replacement for Android's builtin Geocoder.

> A class for handling geocoding and reverse geocoding. Geocoding is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate. Reverse geocoding is the process of transforming a (latitude, longitude) coordinate into a (partial) address. The amount of detail in a reverse geocoded location description may vary, for example one might contain the full street address of the closest building, while another might contain only a city name and postal code.

### Why

Android's implementation of [Geocoder](https://developer.android.com/reference/android/location/Geocoder.html)
requires the a backend service that is not included in the code android framework.
On devices where this backend framework is absent, all calls to the Android Geocoder
will silently fail. Additionally, the backend implementation of the android Geocoder may
vary across manufacturers, devices and android versions. This library was created
to provide a consistent experience across all devices and users.


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

[ ] Remove RxJava from API in favor of Kotlin coroutines.

[ ] Match method signature of Android's Geocoder.

[ ] Use Android Platform Address object

[ ] Streaming JSON parsing



## Contributing to Patrons

To report a bug or enhancement request, feel free to file an issue under the respective heading.

If you wish to contribute to the project, fork this repo and submit a pull request. Code contributions should follow the standards specified in the [Prolific Android Style Guide](https://github.com/prolificinteractive/android-code-styles).

## License

![prolific](https://s3.amazonaws.com/prolificsitestaging/logos/Prolific_Logo_Full_Color.png)

Copyright (c) 2017 Prolific Interactive

Patrons is maintained and sponsored by Prolific Interactive. It may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: ./LICENSE
