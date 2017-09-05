package com.prolificinteractive.geocoder.api.okhttp

import com.prolificinteractive.geocoder.Downloader
import io.reactivex.disposables.Disposable
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class OkHttpDownloader private constructor(
    private val client: OkHttpClient,
    private val headers: Map<String, String>) : Downloader {
  private var call: Call? = null

  override val disposable: Disposable
    get() = object : Disposable {
      override fun dispose() {
        call?.cancel()
      }

      override fun isDisposed(): Boolean {
        return call?.isCanceled == true
      }
    }

  @Throws(IOException::class)
  override fun request(url: String): ByteArray {
    val requestBuilder = Request.Builder()
        .url(url)
        .get()

    for ((k, v) in headers) {
      requestBuilder.addHeader(k, v)
    }

    call = client
        .newCall(requestBuilder.build())

    return call!!.execute().body()?.bytes()!!
  }

  class OkHttpFactory(private val client: OkHttpClient) : Downloader.Factory {

    private val headers: Map<String, String> = HashMap()

    fun addHeaders(headers: Map<String, String>): OkHttpFactory {
      this.headers.plus(headers)
      return this
    }

    override fun create(): Downloader {
      return OkHttpDownloader(client, headers)
    }
  }
}