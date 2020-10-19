package com.raywenderlich.android.taskie.model.response

import kotlinx.serialization.Serializable

/**
 * Holds the message response from the delete note request.
 */
@Serializable
class DeleteNoteResponse(val message: String)