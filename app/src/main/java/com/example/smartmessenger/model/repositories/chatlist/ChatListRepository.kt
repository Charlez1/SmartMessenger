package com.example.smartmessenger.model.repositories.chatlist

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.repositories.entity.Message
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {

    suspend fun getPagedDialogList(): Flow<PagingData<ChatItem>>

    suspend fun createDialog()


}