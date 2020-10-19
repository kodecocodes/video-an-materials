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

package com.raywenderlich.android.taskie.ui.register

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raywenderlich.android.taskie.App
import com.raywenderlich.android.taskie.R
import com.raywenderlich.android.taskie.model.Success
import com.raywenderlich.android.taskie.model.request.UserDataRequest
import com.raywenderlich.android.taskie.networking.NetworkStatusChecker
import com.raywenderlich.android.taskie.utils.gone
import com.raywenderlich.android.taskie.utils.toast
import com.raywenderlich.android.taskie.utils.visible
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Displays the Register screen, with the options to register, or head over to Login!
 */
class RegisterActivity : AppCompatActivity() {

  private val remoteApi = App.remoteApi

  private val networkStatusChecker by lazy {
    NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    initUi()
  }

  private fun initUi() {
    register.setOnClickListener {
      processData(nameInput.text.toString(), emailInput.text.toString(),
          passwordInput.text.toString())
    }
  }

  private fun processData(username: String, email: String, password: String) {
    if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
      networkStatusChecker.performIfConnectedToInternet {
        remoteApi.registerUser(UserDataRequest(email, password, username)) { result ->
          if (result is Success) {
            toast(result.data)
            onRegisterSuccess()
          } else {
            onRegisterError()
          }
        }
      }
    } else {
      onRegisterError()
    }
  }

  private fun onRegisterSuccess() {
    errorText.gone()
    finish()
  }

  private fun onRegisterError() {
    errorText.visible()
  }
}