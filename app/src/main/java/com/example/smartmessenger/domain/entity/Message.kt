package com.example.smartmessenger.domain.entity

import java.util.Date

data class Message(
    val messageId: String,
    val sender: String,
    val messageText: String,
    val timestamp: Date
)

