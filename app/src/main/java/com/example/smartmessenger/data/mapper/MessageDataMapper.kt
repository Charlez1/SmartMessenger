package com.example.smartmessenger.data.mapper

import com.example.smartmessenger.data.model.MessageTransaction
import com.example.smartmessenger.domain.entity.Message
import java.util.Date

object MessageDataMapper {

    fun MessageTransaction.mapMessageTransactionToMessage(): Message? {
        return Message(
            messageId = this.messageId,
            sender = this.sender,
            messageText = this.messageText,
            timestamp = this.timestamp ?: return null
        )
    }

    fun List<MessageTransaction>.mapMessageTransactionListToMessageList(): List<Message> {
        val messageList = mutableListOf<Message>()
        this.forEach {
            val message = it.mapMessageTransactionToMessage()
            if (message != null)
                messageList.add(message)
        }
        return messageList
    }

}