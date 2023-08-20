package com.example.smartmessenger.data.source.chats

import com.example.smartmessenger.data.Constants.CHATS_COLLECTION
import com.example.smartmessenger.data.Constants.CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY
import com.example.smartmessenger.data.Constants.CHATS_PARTICIPANTS_IDS_PROPERTY
import com.example.smartmessenger.data.model.ChatTransaction
import com.example.smartmessenger.utils.ConnectionException
import com.example.smartmessenger.utils.UnknownException
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.domain.entity.Chat
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreChatsSource @Inject constructor(
    private val database: FirebaseFirestore
) : ChatsSource {

    override suspend fun getChatListSnapshot(usetId: String, limit: Int, offset: DocumentSnapshot?): QuerySnapshot {
        val chatReference = database.collection(CHATS_COLLECTION)
        val query = chatReference
            .whereArrayContains(CHATS_PARTICIPANTS_IDS_PROPERTY, usetId)
            .orderBy(CHATS_LAST_MESSAGE_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .apply {
                if (offset != null)
                    this.startAfter(offset)
            }
        return saveFirebaseExecute {
            query.get().await()
        }
    }

    override suspend fun getChatData(chatId: String): ChatTransaction {
        val chatReference = database.collection(CHATS_COLLECTION)
        val document = chatReference.document(chatId)
        return saveFirebaseExecute {
            document.get().await().toObject(ChatTransaction::class.java) ?: throw IllegalStateException("Failed to convert document to ChatTransaction")
        }

    }

    override suspend fun createChat(chat: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun setLastMessageForChat(chatId: String, message: Message) {
        val chatReference = database.collection("chats").document(chatId)
        chatReference.update(
            hashMapOf(
                "last_message_timestamp" to message.timestamp,
                "last_message_text" to message.messageText
            ) as Map<String, Any>
        )
    }

    private suspend fun <T> saveFirebaseExecute(block: suspend () -> T) : T {
        return try {
            block()
        } catch (exception: FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(exception.message ?: "")
                else -> throw UnknownException(exception.message ?: "")
            }
        }
    }
}