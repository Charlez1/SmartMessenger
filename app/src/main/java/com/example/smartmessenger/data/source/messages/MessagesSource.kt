package com.example.smartmessenger.data.source.messages

import com.example.smartmessenger.data.model.MessageTransaction
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.domain.repository.currentchat.MessageCallback
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface MessagesSource {

    suspend fun loadMessages(chatId: String, limit: Int, startAfter: DocumentSnapshot?, endBefore: DocumentSnapshot?): QuerySnapshot

    suspend fun sendMessage(chatId: String, messageRequest: MessageTransaction)

    suspend fun setLastMessage(chatId: String, message: Message)

    fun setUpHandlerNewMessageChanges(chatId: String)

    fun removeObserver(observer: MessageCallback)

    fun addObserver(observer: MessageCallback)


}