package com.example.smartmessenger.model.repositories.account

import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.EmptyFieldException
import com.example.smartmessenger.SignUpData
import com.example.smartmessenger.model.Field
import com.example.smartmessenger.model.source.users.UsersSource
import com.example.smartmessenger.model.source.auth.AuthenticationSource

class AccountsRepositoryImpl(
    private val authenticationSource: AuthenticationSource,
    private val usersSource: UsersSource,
    private val appSettings: AppSettings
) : AccountsRepository {

    override suspend fun signIn(email: String, password: String, rememberUser: Boolean) {
        if(email.isBlank()) throw (EmptyFieldException(Field.Email))
        if(password.isBlank()) throw (EmptyFieldException(Field.Password))

        authenticationSource.signIn(email, password)
        val uId = authenticationSource.getCurrentUId()
        appSettings.setCurrentUId(uId)
        appSettings.setIsRememberUser(rememberUser)
    }

    override suspend fun signUp(singUpData: SignUpData) {
        singUpData.validate()
        authenticationSource.signUp(singUpData.email, singUpData.password) // create account in firebase authentication
        val uId = authenticationSource.getCurrentUId()
        usersSource.createAccount(uId ,singUpData.username, singUpData.email) // create account in firebase realtime
    }

}