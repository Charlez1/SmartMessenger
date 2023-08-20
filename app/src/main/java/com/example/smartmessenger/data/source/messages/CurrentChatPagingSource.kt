package com.example.smartmessenger.data.source.messages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.smartmessenger.data.mapper.ChatDataMapper.mapChatTransactionListToChatList
import com.example.smartmessenger.data.mapper.MessageDataMapper.mapMessageTransactionListToMessageList
import com.example.smartmessenger.data.model.ChatTransaction
import com.example.smartmessenger.data.model.MessageTransaction
import com.example.smartmessenger.data.repository.currentchat.MessagesPageLoader
import com.example.smartmessenger.domain.entity.Message
import com.google.firebase.firestore.QuerySnapshot

class CurrentChatPagingSource(
    private val loader: MessagesPageLoader
) : PagingSource<QuerySnapshot, Message>() {

    private var currentPage: QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Message> {
        return try {
            val currentPageKey = params.key ?: loader.invoke(params.loadSize, null, null)
            currentPage = currentPageKey
            val lastLoadedDocument = currentPageKey.documents.last()
            val firstLoadedDocument = currentPageKey.documents.first()
            val nextPageKey = loader.invoke(params.loadSize, lastLoadedDocument, null)
            val prevPageKey = loader.invoke(params.loadSize, null, firstLoadedDocument)
            val currentChatResponse = currentPageKey.toObjects(MessageTransaction::class.java)
            return LoadResult.Page(
                data = currentChatResponse.mapMessageTransactionListToMessageList(),
                prevKey = if (currentPageKey.documents != prevPageKey.documents) prevPageKey else null,
                nextKey = if (currentPageKey.documents != nextPageKey.documents) nextPageKey else null
            )

        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Message>): QuerySnapshot? {
        val anchorPosition = state.anchorPosition ?: return null
        val currentPage = state.pages.getOrNull(anchorPosition / state.config.pageSize - 1)

        return currentPage?.nextKey
    }
}