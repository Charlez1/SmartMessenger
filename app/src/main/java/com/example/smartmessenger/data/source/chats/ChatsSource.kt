package com.example.smartmessenger.data.source.chats

import com.example.smartmessenger.data.model.ChatTransaction
import com.example.smartmessenger.domain.entity.Interlocutor
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.data.source.users.UserData
import com.example.smartmessenger.domain.entity.Chat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface ChatsSource {

    suspend fun getChatListSnapshot(userId: String, limit: Int, offset: DocumentSnapshot?): QuerySnapshot

    suspend fun getChatData(chatId: String) : ChatTransaction

    suspend fun createChat(chat: Chat)

    suspend fun setLastMessageForChat(chatId: String, message: Message)
}