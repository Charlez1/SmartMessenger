package com.example.smartmessenger.data.source.auth

interface AuthenticationSource {

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String)

    suspend fun deleteCurrentUser()

    fun getCurrentUId(): String
}