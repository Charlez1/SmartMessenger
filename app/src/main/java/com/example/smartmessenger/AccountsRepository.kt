package com.example.smartmessenger

interface AccountsRepository {

    suspend fun signIn(email: String, password: String)

    suspend fun signInByToken(token: String)

    suspend fun signUp(singUpData: SignUpData)

}