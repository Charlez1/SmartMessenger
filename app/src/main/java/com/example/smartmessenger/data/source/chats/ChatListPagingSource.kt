package com.example.smartmessenger.data.source.chats

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.smartmessenger.data.mapper.ChatDataMapper.mapChatTransactionListToChatList
import com.example.smartmessenger.data.model.ChatTransaction
import com.example.smartmessenger.data.repository.chatlist.ChatsPageLoader
import com.example.smartmessenger.domain.entity.Chat
import com.google.firebase.firestore.QuerySnapshot

class ChatListPagingSource(
    private val loader: ChatsPageLoader,
    private val currentUserId: String
) : PagingSource<QuerySnapshot, Chat>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Chat> {
        return try {
            val currentPageKey = params.key ?: loader.invoke(params.loadSize, null)
            val lastLoadedDocument = currentPageKey.documents.last()
            val nextPageKey = loader.invoke(params.loadSize, lastLoadedDocument)
            val chatListResponse = currentPageKey.toObjects(ChatTransaction::class.java)
            return LoadResult.Page(
                data = chatListResponse.mapChatTransactionListToChatList(currentUserId),
                prevKey = null,
                nextKey = if (currentPageKey != nextPageKey) nextPageKey else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Chat>): QuerySnapshot? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.nextKey
    }
}