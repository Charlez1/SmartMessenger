package com.example.smartmessenger.presentation.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.smartmessenger.utils.LiveEvent
import com.example.smartmessenger.utils.MutableLiveEvent
import com.example.smartmessenger.utils.publishEvent
import com.example.smartmessenger.domain.entity.Chat
import com.example.smartmessenger.domain.repository.chatlist.ChatListRepository
import com.example.smartmessenger.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: ChatListRepository
) : BaseViewModel(), DialogsListActionListener {

    var dialogsList: Flow<PagingData<Chat>> = repository.getPagedChatList()

    private val _loadDialogListInProgress = MutableLiveData<Boolean> ()
    val loadDialogListInProgress: LiveData<Boolean> = _loadDialogListInProgress

    private val _navigateToCurrentChat = MutableLiveEvent<String> ()
    val navigateToCurrentChat: LiveEvent<String> = _navigateToCurrentChat

    fun loadDialogsList() = safeLaunch {
        setProgressVisibility(true)
        try {

        } catch (e: Exception) {


        } finally {
            setProgressVisibility(false)
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        _loadDialogListInProgress.postValue(isVisible)
    }

    override fun navigateToChat(chatId: String) = _navigateToCurrentChat.publishEvent(chatId)

}