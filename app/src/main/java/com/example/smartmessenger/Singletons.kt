package com.example.smartmessenger

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Singletons {


    private lateinit var appContext: Context

    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    // --- sources

    private val accountsSource: AccountSource by lazy {
        FirebaseAccountsSource(firebaseAuth)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        AccountsFirebaseRepository(
            accountSource = accountsSource,
            appSettings = appSettings

        )
    }


    fun init(appContext: Context) {
        Singletons.appContext = appContext
    }
}