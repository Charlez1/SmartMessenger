package com.example.smartmessenger.model.repositories.account

import com.example.smartmessenger.SignUpData

interface AccountsRepository {

    suspend fun signIn(email: String, password: String, rememberUser: Boolean)

    suspend fun signUp(singUpData: SignUpData)
}