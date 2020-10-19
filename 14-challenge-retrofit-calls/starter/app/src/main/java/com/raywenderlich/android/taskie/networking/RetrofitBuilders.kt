package com.raywenderlich.android.taskie.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Builds retrofit dependencies.
 */
fun buildClient(): OkHttpClient =
    OkHttpClient.Builder()
        .build()

fun buildRetrofit(): Retrofit {
  return Retrofit.Builder()
      .client(buildClient())
      .baseUrl(BASE_URL)
      .build()
}

fun buildApiService(): RemoteApiService =
    buildRetrofit().create(RemoteApiService::class.java)