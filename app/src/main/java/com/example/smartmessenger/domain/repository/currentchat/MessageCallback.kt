package com.example.smartmessenger.domain.repository.currentchat

import com.example.smartmessenger.domain.entity.Message

interface MessageCallback {

    fun onNewMessage(message: Message)
}