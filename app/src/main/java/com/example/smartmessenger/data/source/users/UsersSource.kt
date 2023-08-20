package com.example.smartmessenger.data.source.users

interface UsersSource {

    suspend fun createAccount(uId: String, userData: UserData)

    suspend fun getUserData(uId: String): UserData

    suspend fun checkUniqueName(uniqueName: String): Boolean

    suspend fun deleteUser(uId: String)

    suspend fun getUserDataByUniqueName(uniqueName: String): UserData
}

data class UserData(
    val email: String,
    val username: String,
    val uniqueName: String
)