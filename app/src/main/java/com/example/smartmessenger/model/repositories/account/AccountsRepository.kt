package com.example.smartmessenger.model.repositories.account

import com.example.smartmessenger.SignUpData

interface AccountsRepository {

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(singUpData: SignUpData)
}