package com.example.smartmessenger.data

object Constants {
    const val CHATS_COLLECTION = "chats"
    const val CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY = "last_message_timestamp"
    const val CHATS_LAST_MESSAGE_TEXT_PROPERTY = "last_message_text"
    const val CHATS_PARTICIPANTS_IDS_PROPERTY = "participants_ids"
    const val CHATS_PARTICIPANTS_NAMES_PROPERTY = "participants_names"
    const val CHATS_PARTICIPANTS_HAS_UNREAD_MESSAGES_PROPERTY = "has_unread_messages"

    const val MESSAGES_COLLECTION = "messages"
    const val ONE_CHAT_MESSAGES_COLLECTION = "one_chat_messages"
    const val MESSAGES_SENDER_PROPERTY = "sender"
    const val MESSAGES_TEXT_PROPERTY = "text"
    const val MESSAGES_TIMESTAMP_PROPERTY = "timestamp"

    const val USERS_COLLECTION = "users"
    const val USERS_EMAIL_PROPERTY = "email"
    const val USERS_NAME_PROPERTY = "name"
    const val USERS_UNIQUE_NAME_PROPERTY = "unique_name"

}