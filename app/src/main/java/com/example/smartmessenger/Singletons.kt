package com.example.smartmessenger

import android.content.Context
import com.example.smartmessenger.model.repositories.account.AccountsRepositoryImpl
import com.example.smartmessenger.model.repositories.account.AccountsRepository
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepositoryImpl
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepository
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepository
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepositoryImpl
import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.model.settings.SharedPreferencesAppSettings
import com.example.smartmessenger.model.source.users.UsersSource
import com.example.smartmessenger.model.source.users.FirestoreUsersSource
import com.example.smartmessenger.model.source.auth.AuthenticationSource
import com.example.smartmessenger.model.source.auth.FirebaseAuthenticationSource
import com.example.smartmessenger.model.source.chats.ChatsSource
import com.example.smartmessenger.model.source.chats.FirestoreChatsSource
import com.example.smartmessenger.model.source.messages.MessagesPagingManager
import com.example.smartmessenger.model.source.messages.MessagesSource
import com.example.smartmessenger.model.source.messages.FirestoreMessagesSource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Singletons {

    private lateinit var appContext: Context

    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val firebaseDatabase by lazy {
        Firebase.firestore
    }

    val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    // --- sources

    private val authenticationSource: AuthenticationSource by lazy {
        FirebaseAuthenticationSource(firebaseAuth)
    }

    private val accountsSource: UsersSource by lazy {
        FirestoreUsersSource(firebaseDatabase)
    }

    private val chatsSource: ChatsSource by lazy {
        FirestoreChatsSource(firebaseDatabase)
    }

    private val messagesSource: MessagesSource by lazy {
        FirestoreMessagesSource(firebaseDatabase, messagesPagingManager)
    }

    private val messagesPagingManager: MessagesPagingManager by lazy {
        MessagesPagingManager()
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        AccountsRepositoryImpl(
            authenticationSource = authenticationSource,
            usersSource = accountsSource,
            appSettings = appSettings
        )
    }

    val chatItemsRepository: ChatListRepository by lazy {
        ChatListRepositoryImpl(
            dialogsSource = chatsSource,
            appSettings = appSettings
        )
    }

    val currentChatRepository: CurrentChatRepository by lazy {
        CurrentChatRepositoryImpl(
            messagesSource = messagesSource,
            chatsSource = chatsSource,
            accountsSource = accountsSource,
            appSettings = appSettings
        )
    }

    fun init(appContext: Context) {
        Singletons.appContext = appContext
    }
}