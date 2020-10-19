package com.raywenderlich.android.taskie.model.response

import com.squareup.moshi.Json

/**
 * Holds a message response after a note is completed.
 */
class CompleteNoteResponse(@field:Json(name = "message") val message: String?)