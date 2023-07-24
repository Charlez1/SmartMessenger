package com.example.smartmessenger.model.repositories.entity

import com.google.firebase.firestore.FieldValue

data class Message(
    val timestamp: String,
    val sender: String,
    val messageText: String
)

data class MessageData(
    val timestamp: FieldValue,
    val sender: String,
    val messageText: String
)

