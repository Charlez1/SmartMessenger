package com.example.smartmessenger.model.source.chatlist

import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseChatListSource(
    private val database: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : ChatListSource {

    override suspend fun getDialogsList(uId: String): List<ChatItem> {
        try {
            val dialogsList = mutableListOf<ChatItem>()

            val usersReference = database.getReference("users")

            val userSnapshot = usersReference.child(uId).get().await()
            val userDialogs = userSnapshot.child("dialogs").value as? Map<String, Boolean>
            val chatsReference = database.getReference("chats")

            userDialogs?.keys?.forEach { chatId ->
                val chatSnapshot = chatsReference.child(chatId).get().await()
                val firstMember = chatSnapshot.child("first_member").value as? String
                val secondMember = chatSnapshot.child("second_member").value as? String
                val dialog = ChatItem(
                    id = chatId,
                    lastMessage = (chatSnapshot.child("last_message").value as? String).orEmpty(),
                    currentMember = uId,
                    anotherMember = (if (secondMember == uId) firstMember else secondMember).orEmpty())
                dialogsList.add(dialog)
            }

            return dialogsList
        } catch (e: Exception) {
            // Обработка ошибок при чтении данных
        }
        return emptyList()
    }

    override suspend fun createDialog() {
        TODO("Not yet implemented")
    }
}