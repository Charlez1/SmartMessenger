package com.example.smartmessenger.model.repositories.chatitem

import com.example.smartmessenger.AppSettings
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.source.chatlist.ChatListSource

class ChatItemFirebaseRepository(
    private val dialogsSource: ChatListSource,
    private val appSettings: AppSettings
) : ChatItemRepository {


    override suspend fun getDialogList(): List<ChatItem> {
        return try {
            dialogsSource.getDialogsList(appSettings.getCurrentUId() ?: "")
        } catch (e: Exception) {
            emptyList()
        }
    }
}