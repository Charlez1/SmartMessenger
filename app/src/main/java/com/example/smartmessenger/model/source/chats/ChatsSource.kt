package com.example.smartmessenger.model.source.chats

import com.example.smartmessenger.model.repositories.entity.Message

interface ChatsSource {
    suspend fun getDialogDataList(uId: String, limit: Int, offset: Int): List<DialogData>

    suspend fun getDialogData(chatId: String): DialogData

    suspend fun createDialog()

    suspend fun setLastMessage(chatId: String, message: Message)
}

data class DialogData(
    val dialogId: String,
    val firstMemberId: String,
    val secondMemberId: String,
    val lastMessageText: String,
    val lastMessageTimestamp: String
)