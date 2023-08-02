package com.example.smartmessenger.di

import com.example.smartmessenger.model.source.auth.AuthenticationSource
import com.example.smartmessenger.model.source.auth.FirebaseAuthenticationSource
import com.example.smartmessenger.model.source.chats.ChatsSource
import com.example.smartmessenger.model.source.chats.FirestoreChatsSource
import com.example.smartmessenger.model.source.messages.FirestoreMessagesSource
import com.example.smartmessenger.model.source.messages.MessagesSource
import com.example.smartmessenger.model.source.users.FirestoreUsersSource
import com.example.smartmessenger.model.source.users.UsersSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindAuthenticationSource(
        firebaseAuthenticationSource: FirebaseAuthenticationSource
    ): AuthenticationSource

    @Binds
    abstract fun bindChatsSource(
        firestoreChatsSource: FirestoreChatsSource
    ): ChatsSource

    @Binds
    abstract fun bindUsersSource(
        firestoreUsersSource: FirestoreUsersSource
    ): UsersSource

    @Binds
    abstract fun bindMessagesSource(
        firestoreMessagesSource: FirestoreMessagesSource
    ): MessagesSource

}