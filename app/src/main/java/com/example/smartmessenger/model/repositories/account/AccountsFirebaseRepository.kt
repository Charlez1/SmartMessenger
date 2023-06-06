package com.example.smartmessenger.model.repositories.account

import com.example.smartmessenger.AppSettings
import com.example.smartmessenger.EmptyFieldException
import com.example.smartmessenger.SignUpData
import com.example.smartmessenger.model.Field
import com.example.smartmessenger.model.source.account.AccountSource
import com.example.smartmessenger.model.source.auth.AuthenticationSource

class AccountsFirebaseRepository(
    private val authenticationSource: AuthenticationSource,
    private val accountSource: AccountSource,
    private val appSettings: AppSettings
) : AccountsRepository {

    override suspend fun signIn(email: String, password: String) {
        if(email.isBlank()) throw (EmptyFieldException(Field.Email))
        if(password.isBlank()) throw (EmptyFieldException(Field.Password))

        authenticationSource.signIn(email, password,
            setCurrentUId = { appSettings.setCurrentUId(it) })
    }

    override suspend fun signUp(singUpData: SignUpData) {
        singUpData.validate()
        authenticationSource.signUp(singUpData.username, singUpData.email, singUpData.password) // create account in firebase authentication
        val uId = authenticationSource.getCurrentUId()
        accountSource.createAccount(uId ,singUpData.username, singUpData.email) // create account in firebase realtime
    }

}