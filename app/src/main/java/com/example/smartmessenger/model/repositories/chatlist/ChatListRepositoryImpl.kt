package com.example.smartmessenger.model.repositories.chatlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.source.chats.ChatsSource
import com.example.smartmessenger.model.source.chats.DialogData
import com.example.smartmessenger.model.source.pagingsource.ChatListPagingSource
import kotlinx.coroutines.flow.Flow

typealias ChatsPageLoader = suspend (offset: Int, pageSize: Int) -> List<ChatItem>


class ChatListRepositoryImpl(
    private val dialogsSource: ChatsSource,
    private val appSettings: AppSettings
) : ChatListRepository {


    override suspend fun getPagedDialogList(): Flow<PagingData<ChatItem>> {

        val loader: ChatsPageLoader = { offset, limit ->
            val dialogList = dialogsSource.getDialogDataList(appSettings.getCurrentUId() ?: "", limit, offset)
            val chatItemList = mutableListOf<ChatItem>()
            dialogList.forEach { dialogData ->
                chatItemList.add(mapDialogDataToChatItem(dialogData))
            }
            chatItemList
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ChatListPagingSource(loader) }
        ).flow
    }

    override suspend fun createDialog() {
        TODO("Not yet implemented")
    }

    private fun mapDialogDataToChatItem(dialogData: DialogData) : ChatItem {
        return ChatItem(
            id = dialogData.dialogId,
            anotherMemberUsername =
            if (appSettings.getCurrentUId() == dialogData.firstMemberId)
                dialogData.secondMemberId
            else
                dialogData.firstMemberId,
            lastMessage = dialogData.lastMessageText,
            timestamp = dialogData.lastMessageTimestamp,
            countUncheckedMessages = 0
        )
    }

    private companion object {
        const val PAGE_SIZE = 15
    }
}