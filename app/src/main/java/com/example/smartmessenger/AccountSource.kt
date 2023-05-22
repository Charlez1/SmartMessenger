package com.example.smartmessenger

interface AccountSource {

    suspend fun signIn(email: String, password: String)

    suspend fun getToken(): String?

    suspend fun signUp(email: String, password: String)

    suspend fun singInByToken(token: String)

}