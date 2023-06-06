package com.example.smartmessenger.model.repositories.entity

data class ChatItem(
    val id: String,
    var lastMessage: String,
    val currentMember: String,
    val anotherMember: String
)