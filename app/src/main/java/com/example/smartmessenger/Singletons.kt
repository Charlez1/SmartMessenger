package com.example.smartmessenger

import android.content.Context
import com.example.smartmessenger.model.repositories.account.AccountsFirebaseRepository
import com.example.smartmessenger.model.repositories.account.AccountsRepository
import com.example.smartmessenger.model.repositories.chatitem.ChatItemFirebaseRepository
import com.example.smartmessenger.model.repositories.chatitem.ChatItemRepository
import com.example.smartmessenger.model.source.account.AccountSource
import com.example.smartmessenger.model.source.account.FirebaseAccountSource
import com.example.smartmessenger.model.source.auth.AuthenticationSource
import com.example.smartmessenger.model.source.auth.FirebaseAuthenticationSource
import com.example.smartmessenger.model.source.chatlist.ChatListSource
import com.example.smartmessenger.model.source.chatlist.FirebaseChatListSource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Singletons {


    private lateinit var appContext: Context

    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val firebaseDatabase by lazy {
        Firebase.database
    }

    val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    // --- sources

    private val authenticationSource: AuthenticationSource by lazy {
        FirebaseAuthenticationSource(firebaseAuth)
    }

    private val accountsSource: AccountSource by lazy {
        FirebaseAccountSource(firebaseDatabase)
    }

    private val dialogsSource: ChatListSource by lazy {
        FirebaseChatListSource(firebaseDatabase, firebaseAuth)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        AccountsFirebaseRepository(
            authenticationSource = authenticationSource,
            accountSource = accountsSource,
            appSettings = appSettings
        )
    }

    val chatItemsRepository: ChatItemRepository by lazy {
        ChatItemFirebaseRepository(
            dialogsSource = dialogsSource,
            appSettings = appSettings
        )
    }

    fun init(appContext: Context) {
        Singletons.appContext = appContext
    }
}