package com.raywenderlich.android.taskie.networking

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Holds the API calls for the Taskie app.
 */
interface RemoteApiService {

  @POST("/api/register")
  fun registerUser(@Body request: RequestBody): Call<ResponseBody>

  @GET("/api/note")
  fun getNotes(@Header("Authorization") token: String): Call<ResponseBody>

  @POST("/api/login")
  fun loginUser(@Body request: RequestBody): Call<ResponseBody>

  @GET("/api/user/profile")
  fun getMyProfile(@Header("Authorization") token: String): Call<ResponseBody>
}