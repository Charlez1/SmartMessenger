package com.example.smartmessenger.screens.currentchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepository
import com.example.smartmessenger.model.repositories.entity.InterlocutorData
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

typealias HandleNewMessage = (hasNewIncomingMessage: Message) -> Unit

@HiltViewModel
class CurrentChatViewModel @Inject constructor(
    private val repository: CurrentChatRepository
) : BaseViewModel() {

    lateinit var messageList: LiveData<PagingData<Message>>

    private val _messageReceived = MutableLiveData<Message> ()
    val messageReceived: LiveData<Message> = _messageReceived


    private val _interlocutorData = MutableLiveData<InterlocutorData> ()
    val interlocutorData: LiveData<InterlocutorData> = _interlocutorData

    fun getMessages(chatId: String) = safeLaunch {
        try {
            val handleNewMessages: HandleNewMessage = { _messageReceived.value = it }
            messageList = repository.getPagedMessageList(chatId, handleNewMessages)
        } catch (e: Exception) {

        }
    }

    fun getInterlocutorData(chatId: String) = safeLaunch {
        try {
            _interlocutorData.value = repository.getInterlocutorData(chatId)
        } catch (e: Exception) {

        }
    }

    fun setLastMessage(chatId: String, message: Message) = safeLaunch {
        try {
            repository.setLastMessage(chatId, message)
        } catch (e: Exception) {

        }
    }

    fun removeStartAfterDocumentValue() {
        repository.removeStartAfterDocumentValue()
    }


    fun sendMessage(chatId: String, messageText: String) = safeLaunch {
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