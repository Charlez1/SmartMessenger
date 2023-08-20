package com.example.smartmessenger.presentation.currentchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.PagingData
import com.example.smartmessenger.domain.repository.currentchat.CurrentChatRepository
import com.example.smartmessenger.domain.entity.Interlocutor
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.domain.repository.currentchat.MessageCallback
import com.example.smartmessenger.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

@HiltViewModel
class CurrentChatViewModel @Inject constructor(
    private val repository: CurrentChatRepository
) : BaseViewModel(), MessageCallback {

    val messageListFlow: Flow<PagingData<Message>>

    private val chatId = MutableLiveData("")


    private val _newMessageReceived = MutableLiveData<Message> ()
    val newMessageReceived: LiveData<Message> = _newMessageReceived

    private val _interlocutor = MutableLiveData<Interlocutor> ()
    val interlocutor: LiveData<Interlocutor> = _interlocutor

    init {
        messageListFlow = chatId.asFlow()
            .flatMapConcat {
                repository.getPagedMessageList(it)
            }
        repository.setViewModelCallback(this)
    }

    fun getMessages(chatId: String)  {
        this.chatId.postValue(chatId)

    }

    fun getInterlocutorData(chatId: String) = safeLaunch {
        try {
            _interlocutor.postValue(repository.getInterlocutorData(chatId))
        } catch (e: Exception) {

        }
    }

    fun sendMessage(chatId: String, messageText: String) = safeLaunch {
        try {
            repository.sendMessage(chatId, messageText)
        } catch (e: Exception) {

        }
    }


    override fun onNewMessage(message: Message) {
        _newMessageReceived.postValue(message)
    }

    override fun onCleared() {
        repository.removeViewModelCallback()
        super.onCleared()
    }


}