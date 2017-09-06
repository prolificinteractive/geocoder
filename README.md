### Geocoder

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

[ ] Remove RxJava from API and replace with Kotlin Coroutines

[ ] Use Android Platform Address object

[ ] Streaming JSON parsing



