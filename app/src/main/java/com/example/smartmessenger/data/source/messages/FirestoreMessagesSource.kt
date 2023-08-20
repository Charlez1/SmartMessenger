package com.example.smartmessenger.data.source.messages

import android.util.Log
import com.example.smartmessenger.data.Constants.MESSAGES_COLLECTION
import com.example.smartmessenger.data.Constants.MESSAGES_TIMESTAMP_PROPERTY
import com.example.smartmessenger.data.Constants.ONE_CHAT_MESSAGES_COLLECTION
import com.example.smartmessenger.data.mapper.MessageDataMapper.mapMessageTransactionToMessage
import com.example.smartmessenger.data.model.MessageTransaction
import com.example.smartmessenger.utils.ConnectionException
import com.example.smartmessenger.utils.UnknownException
import com.example.smartmessenger.domain.entity.Message
import com.example.smartmessenger.domain.repository.currentchat.MessageCallback
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreMessagesSource @Inject constructor(
    private val database: FirebaseFirestore
) : MessagesSource {

    private val observers = mutableListOf<MessageCallback>()

    override suspend fun loadMessages(chatId: String, limit: Int, startAfter: DocumentSnapshot?, endBefore: DocumentSnapshot?): QuerySnapshot {
        val messageReference = getMessageReference(chatId)

        var query = messageReference
            .orderBy(MESSAGES_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(limit.toLong())
        if (startAfter != null)
             query = query.startAfter(startAfter)
        if (endBefore != null)
            query = query.endBefore(endBefore)

        return safeFirestoreCall {
            query.get().await()
        }
    }

    override suspend fun sendMessage(chatId: String, messageRequest: MessageTransaction) {
        val chatReference = getMessageReference(chatId)
        chatReference.add(messageRequest)
    }

    override fun setUpHandlerNewMessageChanges(chatId: String) {
        val messageReference = getMessageReference(chatId)
        messageReference
            .whereGreaterThan(MESSAGES_TIMESTAMP_PROPERTY, Timestamp.now())
            .addSnapshotListener { snapshot, e ->

                Log.d("NewMessageListenerBlock", "called setupMessageListener listener block")

                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (documentChange in snapshot.documentChanges) {
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val messageResponse = documentChange.document.toObject(MessageTransaction::class.java)
                            val message = messageResponse.mapMessageTransactionToMessage()
                            if (message != null)
                                handleNewMessage(message)
                        }
                    }
                }
            }
    }

    override suspend fun setLastMessage(chatId: String, message: Message) {
        TODO("Not yet implemented")
    }

    override fun addObserver(observer: MessageCallback) {
        observers.add(observer)
    }

    override fun removeObserver(observer: MessageCallback) {
        observers.remove(observer)
    }


    private fun handleNewMessage(message: Message) {
        for (observer in observers) {
            observer.onNewMessage(message)
        }

    }

    private fun getMessageReference(chatId: String): CollectionReference {
        return database.collection(MESSAGES_COLLECTION)
            .document(chatId)
            .collection(ONE_CHAT_MESSAGES_COLLECTION)
    }

    private suspend fun <T> safeFirestoreCall(call: suspend () -> T): T {
        try {
            return call()
        } catch (exception: FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(
                    exception.message ?: ""
                )

                else -> throw UnknownException(exception.message ?: "")
            }
        }
    }
}