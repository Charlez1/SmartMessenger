package com.example.smartmessenger.data.repository.account

import com.example.smartmessenger.data.settings.AppSettings
import com.example.smartmessenger.utils.EmptyFieldException
import com.example.smartmessenger.utils.NonUniqueNameException
import com.example.smartmessenger.R
import com.example.smartmessenger.domain.repository.account.AccountsRepository
import com.example.smartmessenger.domain.entity.SignUpData
import com.example.smartmessenger.utils.Field
import com.example.smartmessenger.data.source.users.UsersSource
import com.example.smartmessenger.data.source.auth.AuthenticationSource
import com.example.smartmessenger.data.source.users.UserData
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
            if (usersSource.checkUniqueName(singUpData.uniqueName))
                usersSource.createAccount(
                    uId, UserData(
                        email = singUpData.email,
                        username = singUpData.uniqueName.removePrefix("@"),
                        uniqueName = singUpData.uniqueName
                    )
                ) // create account in firestore
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