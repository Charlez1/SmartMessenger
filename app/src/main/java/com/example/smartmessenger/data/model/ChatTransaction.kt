package com.example.smartmessenger.data.model

import com.example.smartmessenger.data.Constants.CHATS_LAST_MESSAGE_TEXT_PROPERTY
import com.example.smartmessenger.data.Constants.CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY
import com.example.smartmessenger.data.Constants.CHATS_PARTICIPANTS_HAS_UNREAD_MESSAGES_PROPERTY
import com.example.smartmessenger.data.Constants.CHATS_PARTICIPANTS_NAMES_PROPERTY
import com.example.smartmessenger.data.Constants.CHATS_PARTICIPANTS_IDS_PROPERTY
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatTransaction (
    @DocumentId
    val chatId: String,

    @get: PropertyName(CHATS_PARTICIPANTS_IDS_PROPERTY)
    @set: PropertyName(CHATS_PARTICIPANTS_IDS_PROPERTY)
    var participantsIds: List<String>,

    @get: PropertyName(CHATS_PARTICIPANTS_NAMES_PROPERTY)
    @set: PropertyName(CHATS_PARTICIPANTS_NAMES_PROPERTY)
    var participantsNames: List<String>,

    @get: PropertyName(CHATS_PARTICIPANTS_HAS_UNREAD_MESSAGES_PROPERTY)
    @set: PropertyName(CHATS_PARTICIPANTS_HAS_UNREAD_MESSAGES_PROPERTY)
    var participantsHasUnreadMessages: List<Boolean>,

    @get: PropertyName(CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY)
    @set: PropertyName(CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY)
    @ServerTimestamp
    var lastMessageTimestamp: Date,

    @get: PropertyName(CHATS_LAST_MESSAGE_TEXT_PROPERTY)
    @set: PropertyName(CHATS_LAST_MESSAGE_TEXT_PROPERTY)
    var lastMessageText: String
) {
    constructor() : this("", emptyList(), emptyList(), emptyList(), Date(0), "")
}