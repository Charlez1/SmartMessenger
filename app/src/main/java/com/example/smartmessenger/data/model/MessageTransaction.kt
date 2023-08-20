package com.example.smartmessenger.data.model

import com.example.smartmessenger.data.Constants.MESSAGES_SENDER_PROPERTY
import com.example.smartmessenger.data.Constants.MESSAGES_TEXT_PROPERTY
import com.example.smartmessenger.data.Constants.MESSAGES_TIMESTAMP_PROPERTY
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class MessageTransaction(
    @DocumentId
    val messageId: String = "",

    @get: PropertyName(MESSAGES_SENDER_PROPERTY)
    @set: PropertyName(MESSAGES_SENDER_PROPERTY)
    var sender: String,

    @get: PropertyName(MESSAGES_TEXT_PROPERTY)
    @set: PropertyName(MESSAGES_TEXT_PROPERTY)
    var messageText: String,

    @get: PropertyName(MESSAGES_TIMESTAMP_PROPERTY)
    @set: PropertyName(MESSAGES_TIMESTAMP_PROPERTY)
    @ServerTimestamp
    var timestamp: Date? = null
)  {
    constructor(): this( "", "", "", Date(0))
}