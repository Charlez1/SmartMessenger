package com.example.smartmessenger.data.mapper

import com.example.smartmessenger.data.model.ChatTransaction
import com.example.smartmessenger.domain.entity.Chat
import com.example.smartmessenger.domain.entity.Interlocutor

object ChatDataMapper {

    private const val FIRST_MEMBER = 0
    private const val SECOND_MEMBER = 1

    fun ChatTransaction.mapChatTransactionToChat(currentUserId: String): Chat {
        val currentMemberPosition = if (this.participantsIds.first() == currentUserId) FIRST_MEMBER else SECOND_MEMBER
        val anotherMemberPosition = if (this.participantsIds.first() == currentUserId) SECOND_MEMBER else FIRST_MEMBER

        return Chat(
            id = this.chatId,
            anotherMemberId = this.participantsIds[anotherMemberPosition],
            anotherMemberUsername = this.participantsNames[anotherMemberPosition],
            lastMessageText = this.lastMessageText,
            lastMessageTimestamp = this.lastMessageTimestamp,
            hasAnotherUnreadMessages = this.participantsHasUnreadMessages[anotherMemberPosition],
            hasMyUnreadMessages = this.participantsHasUnreadMessages[currentMemberPosition]
        )
    }

    fun List<ChatTransaction>.mapChatTransactionListToChatList(currentUserId: String): List<Chat> {
        val chatList = mutableListOf<Chat>()
        this.forEach {
            chatList.add(it.mapChatTransactionToChat(currentUserId))
        }

        return chatList
    }

    fun ChatTransaction.mapChatTransactionToInterlocutor(currentUserId: String): Interlocutor {
        var anotherMemberPosition = if (this.participantsIds.first() == currentUserId) SECOND_MEMBER else FIRST_MEMBER

        return Interlocutor(
            userId = this.participantsIds[anotherMemberPosition],
            userName = this.participantsNames[anotherMemberPosition]
        )
    }
}