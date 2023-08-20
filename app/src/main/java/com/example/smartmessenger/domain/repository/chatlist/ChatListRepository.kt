package com.example.smartmessenger.domain.repository.chatlist

import androidx.paging.PagingData
import com.example.smartmessenger.domain.entity.Chat
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {

    fun getPagedChatList(): Flow<PagingData<Chat>>

}