package com.prolificinteractive.geocoder

import io.reactivex.disposables.Disposable
import java.io.IOException

interface Downloader {
  val disposable: Disposable

  @Throws(IOException::class)
  fun request(url: String): ByteArray

  interface Factory {
    fun create(): Downloader
  }
}