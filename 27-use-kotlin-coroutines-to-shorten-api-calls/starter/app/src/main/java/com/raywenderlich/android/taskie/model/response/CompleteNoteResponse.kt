package com.raywenderlich.android.taskie.model.response

import kotlinx.serialization.Serializable

/**
 * Holds a message response after a note is completed.
 */

@Serializable
class CompleteNoteResponse(val message: String?)