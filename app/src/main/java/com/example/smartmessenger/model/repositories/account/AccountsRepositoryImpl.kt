package com.example.smartmessenger.model.repositories.account

import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.EmptyFieldException
import com.example.smartmessenger.NonUniqueNameException
import com.example.smartmessenger.R
import com.example.smartmessenger.model.repositories.entity.SignUpData
import com.example.smartmessenger.model.Field
import com.example.smartmessenger.model.source.users.UsersSource
import com.example.smartmessenger.model.source.auth.AuthenticationSource
import com.example.smartmessenger.model.source.users.UserData
import com.google.firebase.FirebaseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRepositoryImpl @Inject constructor(
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
        appSettings.setCurrentUser(usersSource.getUserData(uId))
    }

    override suspend fun signUp(singUpData: SignUpData) {
        singUpData.validate()
        authenticationSource.signUp(singUpData.email, singUpData.password) // create account in firebase authentication
        val uId = authenticationSource.getCurrentUId()

        try {
            if (usersSource.checkUniqueName(singUpData.username))
                usersSource.createAccount(uId, UserData(
                    email = singUpData.email,
                    username = singUpData.username.removePrefix("@"),
                    uniqueName = singUpData.username
                )) // create account in firestore
            else
                throw NonUniqueNameException(R.string.username_non_unique_error.toString())
        } catch (e: Exception) {
            authenticationSource.deleteCurrentUser()
            throw e
        }

    }

    override suspend fun deleteUser(uId: String) {
        usersSource.deleteUser(uId)
    }

}