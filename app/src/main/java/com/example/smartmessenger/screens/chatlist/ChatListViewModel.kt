package com.example.smartmessenger.screens.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.repositories.chatitem.ChatItemRepository
import com.example.smartmessenger.screens.BaseViewModel
import com.hfad.fitness.async.PendingResult
import com.hfad.fitness.async.Result

class ChatListViewModel(
    private val repository: ChatItemRepository
) : BaseViewModel(), DialogsListActionListener {


    private val _dialogsList = MutableLiveData<Result<List<ChatItem>>> (PendingResult())
    val dialogsList: LiveData<Result<List<ChatItem>>> = _dialogsList

    private fun loadDialogsList() = viewModelScope.safeLaunch {
        try {
            into(_dialogsList) { repository.getDialogList()}
        } catch (e: Exception) {

        }
    }

    init {
        loadDialogsList()
    }

    override fun navigateToChat(chatId: String) {
        TODO("Not yet implemented")
    }

}