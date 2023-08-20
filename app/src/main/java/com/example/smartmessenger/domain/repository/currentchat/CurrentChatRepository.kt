package com.example.smartmessenger.domain.repository.currentchat

import androidx.paging.PagingData
import com.example.smartmessenger.domain.entity.Interlocutor
import com.example.smartmessenger.domain.entity.Message
import kotlinx.coroutines.flow.Flow

interface CurrentChatRepository {

    fun getPagedMessageList(chatId: String): Flow<PagingData<Message>>

    fun setViewModelCallback(callback: MessageCallback)

    fun removeViewModelCallback()

    suspend fun getInterlocutorData(chatId: String): Interlocutor

    suspend fun sendMessage(chatId: String, messageText: String)
}