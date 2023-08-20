package com.example.smartmessenger.di

import com.example.smartmessenger.domain.repository.account.AccountsRepository
import com.example.smartmessenger.data.repository.account.AccountsRepositoryImpl
import com.example.smartmessenger.domain.repository.chatlist.ChatListRepository
import com.example.smartmessenger.data.repository.chatlist.ChatListRepositoryImpl
import com.example.smartmessenger.domain.repository.currentchat.CurrentChatRepository
import com.example.smartmessenger.data.repository.currentchat.CurrentChatRepositoryImpl
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