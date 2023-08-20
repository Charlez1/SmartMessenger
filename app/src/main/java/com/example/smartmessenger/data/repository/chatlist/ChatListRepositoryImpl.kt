package com.example.smartmessenger.data.repository.chatlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.smartmessenger.domain.repository.chatlist.ChatListRepository
import com.example.smartmessenger.data.settings.AppSettings
import com.example.smartmessenger.domain.entity.Chat
import com.example.smartmessenger.data.source.chats.ChatsSource
import com.example.smartmessenger.data.source.chats.ChatListPagingSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

typealias ChatsPageLoader = suspend (pageSize: Int, offset: DocumentSnapshot?) -> QuerySnapshot

@Singleton
class ChatListRepositoryImpl @Inject constructor(
    private val chatsSource: ChatsSource,
    private val appSettings: AppSettings
) : ChatListRepository {

    override fun getPagedChatList(): Flow<PagingData<Chat>> {
        val loader: ChatsPageLoader = { limit, offset ->
            chatsSource.getChatListSnapshot(appSettings.getCurrentUId() ?: "", limit, offset)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ChatListPagingSource(loader, appSettings.getCurrentUId() ?: "") }
        ).flow
    }

    private companion object {
        const val PAGE_SIZE = 15
    }
}