package com.example.smartmessenger.model.source.account

interface AccountSource {

    fun createAccount(uId: String, username: String, email: String,)
}