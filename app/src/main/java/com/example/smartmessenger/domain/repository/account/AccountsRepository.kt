package com.example.smartmessenger.domain.repository.account

import com.example.smartmessenger.domain.entity.SignUpData

interface AccountsRepository {

    suspend fun signIn(email: String, password: String, rememberUser: Boolean)

    suspend fun signUp(singUpData: SignUpData)

    suspend fun deleteUser(uId: String)

}