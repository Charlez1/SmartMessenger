package com.example.smartmessenger.model.repositories.currentchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.smartmessenger.model.repositories.entity.InterlocutorData
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.model.source.users.UsersSource
import com.example.smartmessenger.model.source.chats.ChatsSource
import com.example.smartmessenger.model.source.messages.MessagesSource
import com.example.smartmessenger.model.source.messages.MessageData
import com.example.smartmessenger.model.source.pagingsource.CurrentChatPagingSource
import com.example.smartmessenger.screens.currentchat.HandleNewMessage
import javax.inject.Inject
import javax.inject.Singleton

typealias MessagesPageLoader = suspend (pageSize: Int, pageIndex: Int) -> List<Message>

@Singleton
class CurrentChatRepositoryImpl @Inject constructor(
    private val messagesSource: MessagesSource,
    private val chatsSource: ChatsSource,
    private val usersSource: UsersSource,
    private val appSettings: AppSettings,
): CurrentChatRepository {

    override suspend fun getPagedMessageList(chatId: String, handleNewMessage: HandleNewMessage): LiveData<PagingData<Message>> {
        val loader: MessagesPageLoader = { limit, pageIndex ->
            messagesSource.loadMessages(chatId, limit, pageIndex, handleNewMessage)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CurrentChatPagingSource(loader) }
        ).flow.asLiveData()
    }

    override suspend fun sendMessage(chatId: String, messageText: String) {
        val messageData = MessageData(
            timestamp = messagesSource.getCurrentTimestamp(),
            sender = appSettings.getCurrentUId() ?: "" ,
            messageText = messageText
        )
        messagesSource.sendMessage(chatId, messageData)
    }

    override suspend fun getInterlocutorData(chatId: String): InterlocutorData {
        val dialogData =  chatsSource.getDialogData(chatId)
        val interlocutorId = if(appSettings.getCurrentUId() == dialogData.firstMemberId) dialogData.secondMemberId else dialogData.firstMemberId
        val userData = usersSource.getUserData(interlocutorId)
        return InterlocutorData(
            nickname = userData.username,
            onlineStatus = ""
        )
    }

    override suspend fun setLastMessage(chatId: String, message: Message) {
        chatsSource.setLastMessage(chatId, message)
    }

    override fun removeStartAfterDocumentValue() {
        messagesSource.removeStartAfterDocumentValue()
    }

    override fun removeNewMessageListener() {
        messagesSource.removeNewMessageListener()

    }

    private companion object {
        const val PAGE_SIZE = 20
    }

}