package com.example.smartmessenger.domain.entity

import java.util.Date

data class Chat(
    val id: String,
    var anotherMemberId: String,
    var anotherMemberUsername: String,
    var lastMessageText: String,
    var lastMessageTimestamp: Date,
    var hasAnotherUnreadMessages: Boolean,
    var hasMyUnreadMessages: Boolean


)