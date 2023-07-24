package com.example.smartmessenger.screens.currentchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepository
import com.example.smartmessenger.model.repositories.entity.InterlocutorData
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.BaseViewModel
import com.firebase.ui.auth.data.model.User

typealias HandleNewMessage = (hasNewIncomingMessage: Boolean) -> Unit

class CurrentChatViewModel(
    private val repository: CurrentChatRepository
) : BaseViewModel() {

    lateinit var messageList: LiveData<PagingData<Message>>

    private val _messageReceived = MutableLiveData<Boolean> ()
    val messageReceived: LiveData<Boolean> = _messageReceived


    private val _interlocutorData = MutableLiveData<InterlocutorData> ()
    val interlocutorData: LiveData<InterlocutorData> = _interlocutorData

    fun getMessages(chatId: String) = viewModelScope.safeLaunch {
        try {
            val handleNewMessage: HandleNewMessage = { _messageReceived.value = it }
            messageList = repository.getPagedMessageList(chatId, handleNewMessage)
        } catch (e: Exception) {

        }
    }

    fun getInterlocutorData(chatId: String) = viewModelScope.safeLaunch {
        try {
            _interlocutorData.value = repository.getInterlocutorData(chatId)
        } catch (e: Exception) {

        }
    }

    fun setLastMessage(chatId: String, message: Message) = viewModelScope.safeLaunch {
        try {
            repository.setLastMessage(chatId, message)
        } catch (e: Exception) {

        }
    }

    fun removeStartAfterDocumentValue() {
        repository.removeStartAfterDocumentValue()
    }


    fun sendMessage(chatId: String, messageText: String) = viewModelScope.safeLaunch {
        try {
            repository.sendMessage(chatId, messageText)
        } catch (e: Exception) {

        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeNewMessageListener()
    }

}