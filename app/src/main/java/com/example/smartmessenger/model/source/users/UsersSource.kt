package com.example.smartmessenger.model.source.users

interface UsersSource {

    suspend fun createAccount(uId: String, username: String, email: String)

    suspend fun getUserData(uId: String): UserData
}

data class UserData(
    val email: String,
    val username: String
)