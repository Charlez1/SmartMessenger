package com.example.smartmessenger.model.source.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.smartmessenger.model.repositories.chatlist.ChatsPageLoader
import com.example.smartmessenger.model.repositories.entity.ChatItem


class ChatListPagingSource(
    private val loader: ChatsPageLoader
) : PagingSource<Int, ChatItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatItem> {
        val pageIndex = params.key ?: 0
        return try {
            val chats = loader.invoke(pageIndex, params.loadSize)
            // success! now we can return LoadResult.Page
            return LoadResult.Page(
                data = chats,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (chats.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChatItem>): Int? {
        // get the most recently accessed index in the users list:
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index:
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate it manually:
        return page.nextKey?.minus(1) ?: page.prevKey?.plus(1)    }
}