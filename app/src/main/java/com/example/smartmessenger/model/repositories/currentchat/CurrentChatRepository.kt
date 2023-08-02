package com.example.smartmessenger.model.repositories.currentchat

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.entity.InterlocutorData
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.currentchat.HandleNewMessage

interface CurrentChatRepository {

    suspend fun getPagedMessageList(chatId: String, handleNewMessage: HandleNewMessage): LiveData<PagingData<Message>>

    fun removeStartAfterDocumentValue()

    fun removeNewMessageListener()

    suspend fun getInterlocutorData(chatId: String): InterlocutorData

    suspend fun sendMessage(chatId: String, messageText: String)

    suspend fun setLastMessage(chatId: String, message: Message)

}