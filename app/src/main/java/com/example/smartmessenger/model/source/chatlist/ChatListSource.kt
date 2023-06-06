package com.example.smartmessenger.model.source.chatlist

import com.example.smartmessenger.model.repositories.entity.ChatItem

interface ChatListSource {

    suspend fun getDialogsList(uId: String): List<ChatItem>

    suspend fun createDialog()
}