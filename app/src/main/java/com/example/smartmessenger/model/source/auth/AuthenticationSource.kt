package com.example.smartmessenger.model.source.auth

interface AuthenticationSource {

    suspend fun signIn(email: String, password: String, setCurrentUId: (String?) -> Unit)

    suspend fun signUp(username: String, email: String, password: String)

    fun getCurrentUId(): String
}