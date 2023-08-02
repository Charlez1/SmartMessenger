package com.example.smartmessenger.di

import com.example.smartmessenger.model.repositories.account.AccountsRepository
import com.example.smartmessenger.model.repositories.account.AccountsRepositoryImpl
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepository
import com.example.smartmessenger.model.repositories.chatlist.ChatListRepositoryImpl
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepository
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAccountRepository(
        accountsRepositoryImpl: AccountsRepositoryImpl
    ): AccountsRepository

    @Binds
    abstract fun bindChatListRepository(
        chatListRepositoryImpl: ChatListRepositoryImpl
    ): ChatListRepository

    @Binds
    abstract fun bindCurrentChatRepository(
        currentChatRepositoryImpl: CurrentChatRepositoryImpl
    ): CurrentChatRepository

}