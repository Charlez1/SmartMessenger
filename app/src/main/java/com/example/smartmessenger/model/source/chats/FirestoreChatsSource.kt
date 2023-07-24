package com.example.smartmessenger.model.source.chats

import com.example.smartmessenger.ConnectionException
import com.example.smartmessenger.UnknownException
import com.example.smartmessenger.model.repositories.entity.Message
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

class FirestoreChatsSource(
    private val database: FirebaseFirestore
) : ChatsSource {

    private var startAfterDocument: DocumentSnapshot? = null

    override suspend fun getDialogDataList(uId: String, limit: Int, offset: Int): List<DialogData> {
        val chatReference = database.collection("chats")
        val query = chatReference
            .whereArrayContains("participants_ids", uId)
            .orderBy("last_message_timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .apply {
                if (startAfterDocument != null) {
                    startAfter(startAfterDocument)
                }
            }
        val dialogList = mutableListOf<DialogData>()
        try {
            val snapshot: QuerySnapshot = query.get().await()
            startAfterDocument = snapshot.documents[snapshot.size() - 1]
                for (document in snapshot.documents) {
                val dialogData = DialogData(
                    dialogId = document.id,
                    firstMemberId = document.getString("first_member_id") ?: "",
                    secondMemberId = document.getString("second_member_id") ?: "",
                    lastMessageText = document.getString("last_message_text") ?: "",
                    lastMessageTimestamp = document.getString("last_message_timestamp") ?: ""
                )
                dialogList.add(dialogData)
            }
            return dialogList
        } catch (e: FirebaseFirestoreException) {
            processingRemainingExceptions(e)
            return emptyList()
        }
    }

    override suspend fun getDialogData(chatId: String): DialogData {
        val chatReference = database.collection("chats")
        val document = chatReference.document(chatId).get().await()
        return DialogData(
            dialogId = document.id,
            firstMemberId = document.getString("first_member_id") ?: "",
            secondMemberId = document.getString("second_member_id") ?: "",
            lastMessageText = document.getString("last_message_text") ?: "",
            lastMessageTimestamp = document.getString("last_message_timestamp") ?: ""
        )
    }

    override suspend fun createDialog() {
        TODO("Not yet implemented")
    }

    override suspend fun setLastMessage(chatId: String, message: Message) {
        TODO("Not yet implemented")
    }

    private fun processingRemainingExceptions(exception: FirebaseFirestoreException) {
        when (exception.code) {
            FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(exception.message ?: "")
            else -> throw UnknownException(exception.message ?: "")
        }
    }
}