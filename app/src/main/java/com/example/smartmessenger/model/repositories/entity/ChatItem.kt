package com.example.smartmessenger.model.repositories.entity

data class ChatItem(
    val id: String,
    var anotherMemberUsername: String,
    var lastMessage: String,
    var timestamp: String,
    var countUncheckedMessages: Int
)