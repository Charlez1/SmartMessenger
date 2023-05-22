package com.example.smartmessenger

import com.example.smartmessenger.model.Field
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await

class AccountsFirebaseRepository(
    private val accountSource: AccountSource,
    private val appSettings: AppSettings
) : AccountsRepository {

    override suspend fun signIn(email: String, password: String) {
        if(email.isBlank()) throw (EmptyFieldException(Field.Email))
        if(password.isBlank()) throw (EmptyFieldException(Field.Password))

        accountSource.signIn(email, password)
        val token = accountSource.getToken()
        if(token != null)
            appSettings.setCurrentToken(token)
    }

    override suspend fun signInByToken(token: String) {
        accountSource.singInByToken(token)
    }

    override suspend fun signUp(singUpData: SignUpData) {

        singUpData.validate()

        accountSource.signUp(singUpData.email, singUpData.password)

    }

}