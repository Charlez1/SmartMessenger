package com.example.smartmessenger.model.repositories.chatlist

import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.entity.ChatItem
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {

    suspend fun getPagedDialogList(): Flow<PagingData<ChatItem>>

    suspend fun createDialog()
}