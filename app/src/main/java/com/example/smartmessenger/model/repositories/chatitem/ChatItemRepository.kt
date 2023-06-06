package com.example.smartmessenger.model.repositories.chatitem

import com.example.smartmessenger.model.repositories.entity.ChatItem

interface ChatItemRepository {

    suspend fun getDialogList(): List<ChatItem>
}