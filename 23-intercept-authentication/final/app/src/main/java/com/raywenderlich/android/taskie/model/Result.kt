package com.raywenderlich.android.taskie.model

/**
 * Represents the Success and Failure cases from the Remote API.
 */
sealed class Result<out T : Any>

data class Success<out T : Any>(val data: T) : Result<T>()

data class Failure(val error: Throwable?) : Result<Nothing>()