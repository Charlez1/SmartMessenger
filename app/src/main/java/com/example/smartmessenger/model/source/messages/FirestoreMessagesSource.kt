package com.example.smartmessenger.model.source.messages

import android.util.Log
import com.example.smartmessenger.ConnectionException
import com.example.smartmessenger.UnknownException
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.currentchat.HandleNewMessage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreMessagesSource @Inject constructor(
    private val database: FirebaseFirestore
) : MessagesSource {

    private lateinit var newMessageListener: ListenerRegistration

    override suspend fun loadMessages(chatId: String, limit: Int, pageIndex: Int, handleNewMessage: HandleNewMessage): List<Message> {
        val chatReference = getChatReference(chatId)

        var query = chatReference
            .orderBy("timestamp", Query.Direction.DESCENDING)
        query = determineLoadDirection(query, pageIndex)
        query.limit(limit.toLong())

        val messageList = mutableListOf<Message>()
        return try {
            val snapshot: QuerySnapshot = query.get().await()

            if (isFirstLoad()) {
                setLastMessageTimestamp(snapshot.documents[0].getTimestamp("timestamp"))
                setUpHandlerNewMessageChanges(chatId, handleNewMessage)
            }
            setManagerParams(
                firstLoadedDocument = snapshot.documents[0],
                lastLoadedDocument = snapshot.documents[snapshot.size() - 1],
                pageIndex = pageIndex
            )

            for (document in snapshot.documents) {
                messageList.add(getMessage(document))
            }
            return messageList
        } catch (e: FirebaseFirestoreException) {
            processingRemainingExceptions(e)
            return emptyList()
        }
    }

    override suspend fun sendMessage(chatId: String, messageData: MessageData) {
        val chatReference = getChatReference(chatId)
        chatReference.add(
            hashMapOf(
                "timestamp" to messageData.timestamp,
                "text" to messageData.messageText,
                "sender" to messageData.sender
            )
        )

    }

    private fun setUpHandlerNewMessageChanges(chatId: String, handleNewMessage: HandleNewMessage) {
        val chatReference = getChatReference(chatId)
        newMessageListener = chatReference
            .whereGreaterThan("timestamp", getLastMessageTimestamp()!!)
            .addSnapshotListener { snapshot, e ->

                Log.d("NewMessageListenerBlock", "called setupMessageListener listener block")

                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (documentChange in snapshot.documentChanges) {
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val message = getMessage(documentChange.document)
                            handleNewMessage(message)
                        }
                    }
                }
            }
    }


    override fun getCurrentTimestamp(): FieldValue {
        return FieldValue.serverTimestamp()
    }


    override fun removeStartAfterDocumentValue() {
        removeManagerData()
    }


    override fun removeNewMessageListener() {
        newMessageListener.remove()
        removeManagerData()

    }

    override suspend fun setLastMessage(chatId: String, message: Message) {
        TODO("Not yet implemented")
    }


    private fun getMessage(documentSnapshot: DocumentSnapshot): Message {
        val timestamp = documentSnapshot.getTimestamp("timestamp") ?: throw Exception()
        val seconds = timestamp?.seconds ?: 0
        val nanoseconds = timestamp?.nanoseconds ?: 0
        return Message(
            timestamp = (seconds * 1000 + nanoseconds / 1000000).toString(),
            sender = documentSnapshot.getString("sender") ?: "",
            messageText = documentSnapshot.getString("text") ?: ""
        )
    }

    private fun getChatReference(chatId: String): CollectionReference {
        return database.collection("messages")
            .document(chatId)
            .collection("one_chat_messages")
    }

    private fun processingRemainingExceptions(exception: FirebaseFirestoreException) {

        when (exception.code) {
            FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(exception.message ?: "")
            else -> throw UnknownException(exception.message ?: "")
        }
    }

    companion object {

        private var firstLoadedDocument: DocumentSnapshot? = null
        private var lastLoadedDocument: DocumentSnapshot? = null
        private var lastMessageTimestamp: Timestamp? = null
        private var pageIndex: Int = 0

        fun setManagerParams(firstLoadedDocument: DocumentSnapshot?,
                             lastLoadedDocument: DocumentSnapshot?,
                             pageIndex: Int) {
            this.firstLoadedDocument = firstLoadedDocument
            this.lastLoadedDocument = lastLoadedDocument
            this.pageIndex = pageIndex
        }

        fun setLastMessageTimestamp(lastMessageTimestamp: Timestamp?) {
            this.lastMessageTimestamp = lastMessageTimestamp
        }

        fun getLastMessageTimestamp(): Timestamp? {
            return this.lastMessageTimestamp
        }

        fun isFirstLoad(): Boolean = firstLoadedDocument == null

        fun determineLoadDirection(query: Query, pageIndex: Int): Query {
            return if(!isFirstLoad()) {
                if(pageIndex > this.pageIndex)
                    query.startAfter(lastLoadedDocument)
                else if(pageIndex < this.pageIndex)
                    query.endBefore(firstLoadedDocument)
                else
                    query.startAt(firstLoadedDocument)
            } else
                query
        }

        fun removeManagerData() {
            firstLoadedDocument = null
            lastLoadedDocument = null
            lastMessageTimestamp = null
            pageIndex = 0
        }

    }
}