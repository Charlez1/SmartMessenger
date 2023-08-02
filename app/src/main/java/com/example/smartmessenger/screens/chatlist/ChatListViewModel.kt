package com.example.smartmessenger.screens.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.smartmessenger.model.LiveEvent
import com.example.smartmessenger.model.MutableLiveEvent
import com.example.smartmessenger.model.publishEvent
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepository
import com.example.smartmessenger.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ChatListRepository
) : BaseViewModel(), DialogsListActionListener {

    lateinit var dialogsList: Flow<PagingData<ChatItem>>

    private val _loadDialogListInProgress = MutableLiveData<Boolean> ()
    val loadDialogListInProgress: LiveData<Boolean> = _loadDialogListInProgress

    private val _navigateToCurrentChat = MutableLiveEvent<String> ()
    val navigateToCurrentChat: LiveEvent<String> = _navigateToCurrentChat

    private fun loadDialogsList() = safeLaunch {
        setProgressVisibility(true)
        try {
            dialogsList = repository.getPagedDialogList()
        } catch (e: Exception) {


        } finally {
            setProgressVisibility(false)
        }
    }

    init {
        loadDialogsList()
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        _loadDialogListInProgress.value = isVisible
    }

    override fun navigateToChat(chatId: String) = _navigateToCurrentChat.publishEvent(chatId)

}