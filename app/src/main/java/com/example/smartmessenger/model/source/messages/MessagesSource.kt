package com.example.smartmessenger.model.source.messages

import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.currentchat.HandleNewMessage
import com.google.firebase.firestore.FieldValue

interface MessagesSource {

    fun removeStartAfterDocumentValue()

    suspend fun loadMessages(chatId: String, limit: Int, pageIndex: Int, handleNewMessage: HandleNewMessage) : List<Message>

    suspend fun sendMessage(chatId: String, messageData: MessageData)

    fun getCurrentTimestamp(): FieldValue

    fun removeNewMessageListener()

    suspend fun setLastMessage(chatId: String, message: Message)

}

data class MessageData(
    val timestamp: FieldValue,
    val sender: String,
    val messageText: String
)