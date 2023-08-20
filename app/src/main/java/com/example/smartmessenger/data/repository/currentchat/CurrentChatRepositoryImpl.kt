package com.example.smartmessenger.data.repository.currentchat

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.smartmessenger.data.mapper.ChatDataMapper.mapChatTransactionToInterlocutor
import com.example.smartmessenger.data.model.MessageTransaction
import com.example.smartmessenger.domain.repository.currentchat.CurrentChatRepository
import com.example.smartmessenger.domain.entity.Interlocutor
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.data.settings.AppSettings
import com.example.smartmessenger.data.source.users.UsersSource
import com.example.smartmessenger.data.source.chats.ChatsSource
import com.example.smartmessenger.data.source.messages.MessagesSource
import com.example.smartmessenger.data.source.messages.CurrentChatPagingSource
import com.example.smartmessenger.domain.repository.currentchat.MessageCallback
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

typealias MessagesPageLoader = suspend (pageSize: Int, startAfter: DocumentSnapshot?, endBefore: DocumentSnapshot?) -> QuerySnapshot

@Singleton
class CurrentChatRepositoryImpl @Inject constructor(
    private val messagesSource: MessagesSource,
    private val chatsSource: ChatsSource,
    private val usersSource: UsersSource,
    private val appSettings: AppSettings,
): CurrentChatRepository {

    private var viewModelCallback: MessageCallback? = null

    override fun getPagedMessageList(chatId: String): Flow<PagingData<Message>> {
        val loader: MessagesPageLoader = { limit, startAfter, endBefore ->
            messagesSource.loadMessages(chatId, limit, startAfter, endBefore)
        }

        messagesSource.addObserver(object : MessageCallback {
            override fun onNewMessage(message: Message) {
                viewModelCallback?.onNewMessage(message)
            }
        })

        messagesSource.setUpHandlerNewMessageChanges(chatId)

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CurrentChatPagingSource(loader) }
        ).flow
    }

    override suspend fun getInterlocutorData(chatId: String): Interlocutor {
        val chatDataResponse = chatsSource.getChatData(chatId)
        return chatDataResponse.mapChatTransactionToInterlocutor(appSettings.getCurrentUId() ?: "")
    }

    override suspend fun sendMessage(chatId: String, messageText: String) {
        val messageRequest = MessageTransaction(
            sender = appSettings.getCurrentUId() ?: "",
            messageText = messageText
        )
        messagesSource.sendMessage(chatId, messageRequest)
    }


    override fun setViewModelCallback(callback: MessageCallback) {
        viewModelCallback = callback
    }

    override fun removeViewModelCallback() {
        viewModelCallback = null
    }

    private companion object {
        const val PAGE_SIZE = 20
    }

}