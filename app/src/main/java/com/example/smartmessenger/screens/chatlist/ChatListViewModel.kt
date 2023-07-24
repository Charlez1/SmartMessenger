package com.example.smartmessenger.screens.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.smartmessenger.model.LiveEvent
import com.example.smartmessenger.model.MutableLiveEvent
import com.example.smartmessenger.model.publishEvent
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepository
import com.example.smartmessenger.screens.BaseViewModel
import kotlinx.coroutines.flow.Flow

class ChatListViewModel(
    private val repository: ChatListRepository
) : BaseViewModel(), DialogsListActionListener {


    lateinit var dialogsList: Flow<PagingData<ChatItem>>

    private val _navigateToCurrentChat = MutableLiveEvent<String> ()
    val navigateToCurrentChat: LiveEvent<String> = _navigateToCurrentChat

    private fun loadDialogsList() = viewModelScope.safeLaunch {
        try {
            dialogsList = repository.getPagedDialogList()
        } catch (e: Exception) {

        }
    }

    init {
        loadDialogsList()
    }


    override fun navigateToChat(chatId: String) = _navigateToCurrentChat.publishEvent(chatId)

}