package com.example.smartmessenger.model.source.messages

import android.util.Log
import com.example.smartmessenger.ConnectionException
import com.example.smartmessenger.UnknownException
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.currentchat.HandleNewMessage
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

class FirestoreMessagesSource(
    private val database: FirebaseFirestore,
    private val messagesPagingManager: MessagesPagingManager
) : MessagesSource {

    private lateinit var newMessageListener: ListenerRegistration

    override suspend fun loadMessages(chatId: String, limit: Int, pageIndex: Int, handleNewMessage: HandleNewMessage): List<Message> {
        val chatReference = getChatReference(chatId)

        var query = chatReference
            .orderBy("timestamp", Query.Direction.DESCENDING)
        query = messagesPagingManager.determineLoadDirection(query, pageIndex)
        query.limit(limit.toLong())

        val messageList = mutableListOf<Message>()
        return try {
            val snapshot: QuerySnapshot = query.get().await()

            if (messagesPagingManager.isFirstLoad()) {
                messagesPagingManager.setLastMessageTimestamp(snapshot.documents[0].getTimestamp("timestamp"))
                setUpHandlerNewMessageChanges(chatId, handleNewMessage)
            }
            messagesPagingManager.setManagerParams(
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
            .whereGreaterThan("timestamp", messagesPagingManager.getLastMessageTimestamp()!!)
            .addSnapshotListener { snapshot, e ->

                Log.d("NewMessageListenerBlock", "called setupMessageListener listener block")

                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (documentChange in snapshot.documentChanges) {
                        if (documentChange.type == DocumentChange.Type.ADDED)
                            handleNewMessage(true)
                    }
                }
            }
    }


    override fun getCurrentTimestamp(): FieldValue {
        return FieldValue.serverTimestamp()
    }


    override fun removeStartAfterDocumentValue() {
        messagesPagingManager.removeManagerData()
    }


    override fun removeNewMessageListener() {
        newMessageListener.remove()
        messagesPagingManager.removeManagerData()

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
}