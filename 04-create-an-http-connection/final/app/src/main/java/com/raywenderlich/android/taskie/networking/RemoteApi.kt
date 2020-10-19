/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.taskie.networking

import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.model.UserProfile
import com.raywenderlich.android.taskie.model.request.AddTaskRequest
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi {

  fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
    onUserLoggedIn("token", null)
  }

  fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
    Thread(Runnable {
      val connection = URL("$BASE_URL/api/register").openConnection() as HttpURLConnection
      connection.requestMethod = "POST"
      connection.setRequestProperty("Content-Type", "application/json")
      connection.setRequestProperty("Accept", "application/json")
      connection.readTimeout = 10000
      connection.connectTimeout = 10000
      connection.doOutput = true
      connection.doInput = true

      val body = "{\"name\":\"${userDataRequest.name}\", \"email\":\"${userDataRequest.email}\", " +
          "\"password\":\"${userDataRequest.password}\"}"

      val bytes = body.toByteArray()

      try {
        connection.outputStream.use { outputStream ->
          outputStream.write(bytes)
        }

        val reader = InputStreamReader(connection.inputStream)

        reader.use { input ->
          val response = StringBuilder()
          val bufferedReader = BufferedReader(input)

          bufferedReader.useLines { lines ->
            lines.forEach {
              response.append(it.trim())
            }
          }

          onUserCreated(response.toString(), null)
        }
      } catch (error: Throwable) {
        onUserCreated(null, error)
      }

      connection.disconnect()
    }).start()
  }

  fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {
    onTasksReceived(listOf(
        Task("id",
            "Wash laundry",
            "Wash the whites and colored separately!",
            false,
            1
        ),
        Task("id2",
            "Do some work",
            "Finish the project",
            false,
            3
        )
    ), null)
  }

  fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
    onTaskDeleted(null)
  }

  fun completeTask(onTaskCompleted: (Throwable?) -> Unit) {
    onTaskCompleted(null)
  }

  fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {
    onTaskCreated(
        Task("id3",
            addTaskRequest.title,
            addTaskRequest.content,
            false,
            addTaskRequest.taskPriority
        ), null
    )
  }

  fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
    onUserProfileReceived(UserProfile("mail@mail.com", "Filip", 10), null)
  }
}