package com.raywenderlich.android.taskie.networking

import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.request.AddTaskRequest
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import com.raywenderlich.android.taskie.model.response.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Holds the API calls for the Taskie app.
 */
interface RemoteApiService {

  @POST("/api/register")
  fun registerUser(@Body request: UserDataRequest): Call<RegisterResponse>

  @GET("/api/note")
  fun getNotes(): Call<GetTasksResponse>

  @POST("/api/login")
  fun loginUser(@Body request: UserDataRequest): Call<LoginResponse>

  @GET("/api/user/profile")
  fun getMyProfile(): Call<UserProfileResponse>

  @POST("/api/note/complete")
  fun completeTask(@Query("id") noteId: String): Call<CompleteNoteResponse>

  @POST("/api/note")
  fun addTask(@Body request: AddTaskRequest): Call<Task>

  @DELETE("/api/note")
  suspend fun deleteNote(@Query("id") noteId: String): DeleteNoteResponse
}