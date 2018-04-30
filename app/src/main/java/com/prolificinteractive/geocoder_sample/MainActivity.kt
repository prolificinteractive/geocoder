package com.prolificinteractive.geocoder_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.prolificinteractive.geocoder.Geocoder
import com.prolificinteractive.geocoder.GeocoderBuilder
import com.prolificinteractive.geocoder.GeocodingApi
import com.prolificinteractive.geocoder.api.googlemaps.GoogleMaps
import com.prolificinteractive.geocoder.api.okhttp.OkHttpDownloader.OkHttpFactory
import com.prolificinteractive.geocoder.api.openstreetmaps.OpenStreetMaps
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  private lateinit var apiSpinner: Spinner
  private lateinit var input: EditText
  private lateinit var result: TextView
  private lateinit var geocoder: Geocoder

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    input = findViewById(R.id.input)
    result = findViewById(R.id.result)
    apiSpinner = findViewById(R.id.api_spinner)

    val arrayAdapter = ArrayAdapter(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        Providers.values().map { p -> p.apiName }
    )

    apiSpinner.adapter = arrayAdapter

    val textWatcher = object : TextWatcher {
      var disposable: CompositeDisposable = CompositeDisposable()

      override fun afterTextChanged(p0: Editable?) {}

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
        disposable.clear()
        disposable.add(Observable.just(text)
            .filter { t -> t.isNotEmpty() }
            .map { text.toString() }
            .delay(1, TimeUnit.SECONDS)
            .flatMap { location ->
              geocoder.getFromLocation(location, 1).toObservable()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe({ i ->
              result.text = i.toString()
            }, { e -> e.printStackTrace() }))
      }
    }

    val okhttpFactory = OkHttpFactory(
        OkHttpClient.Builder()
            .build())
        .addHeaders(
            mapOf(
                HEADER_USER_AGENT to System.getProperty("http.agent"),
                HEADER_REFERER to System.getProperty("https://play.google.com/store/apps/details?id=com.scotts.gro&hl=en")
            )
        )

    apiSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onNothingSelected(p0: AdapterView<*>?) {}

      override fun onItemSelected(parent: AdapterView<*>?, view: View?, index: Int, id: Long) {
        geocoder = GeocoderBuilder()
            .addGeocodingApi(Providers.values()[index].api)
            .setDownloaderFactory(okhttpFactory)
            .build()

        textWatcher.onTextChanged(input.text, 0, 0, 0)
      }
    }

    geocoder = GeocoderBuilder()
        .addGeocodingApi(GoogleMaps.create(""))
        .setDownloaderFactory(okhttpFactory)
        .build()

    input.addTextChangedListener(textWatcher)
  }

  companion object {
    private val HEADER_REFERER = "Referer"
    private val HEADER_USER_AGENT = "User-Agent"
  }
}

enum class Providers(val apiName: String, val api: GeocodingApi) {
  GOOGLE_MAPS("Google Maps", GoogleMaps.create()),
  GOOGLE_MAPS_API_KEY("Google Maps w/ API Key", GoogleMaps.create("AIzaSyDYOU1mwm69bd87iqlgquN6Psq9KmWOyNw")),
  OPEN_STREET_MAPS("Open Street Maps", OpenStreetMaps.create())

}
