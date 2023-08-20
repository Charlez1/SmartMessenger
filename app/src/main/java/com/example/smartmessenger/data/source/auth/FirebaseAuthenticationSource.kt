package com.example.smartmessenger.data.source.auth

import com.example.smartmessenger.utils.AccountAlreadyExistsException
import com.example.smartmessenger.utils.AuthException
import com.example.smartmessenger.utils.AuthWeakPasswordException
import com.example.smartmessenger.utils.BackendException
import com.example.smartmessenger.utils.ConnectionException
import com.example.smartmessenger.utils.InvalidUserException
import com.example.smartmessenger.utils.TooManyRequestsException
import com.example.smartmessenger.utils.UnknownException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthenticationSource @Inject constructor(
    private val auth: FirebaseAuth
): AuthenticationSource {

    override suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (exception: Exception) {
            when (exception) {
                is FirebaseTooManyRequestsException -> throw (TooManyRequestsException(exception.message ?: ""))
                is FirebaseAuthInvalidCredentialsException -> throw (InvalidUserException(exception.message ?: ""))
                else -> processingRemainingExceptions(exception)
            }
        }
    }

    override suspend fun
            signUp(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (exception: Exception) {
            when (exception) {
                is FirebaseAuthWeakPasswordException -> throw AuthWeakPasswordException(exception.message ?: "")
                is FirebaseAuthEmailException -> throw AccountAlreadyExistsException(exception.message ?: "")
                else -> processingRemainingExceptions(exception)
            }
        }
    }

    override suspend fun deleteCurrentUser() {
        auth.currentUser?.delete()?.await()
    }

    private fun processingRemainingExceptions(exception: Exception) {
        when (exception) {
            is FirebaseNetworkException -> throw ConnectionException(exception.message ?: "")
            is FirebaseAuthWebException -> throw BackendException(exception.message ?: "")
            is FirebaseAuthException -> throw AuthException(exception.message ?: "")
            else -> throw UnknownException(exception.message ?: "")
        }
    }

    override fun getCurrentUId(): String {
        try {
            val user = auth.currentUser
            return user?.uid ?: ""
        } catch (exception: Exception) {
            processingRemainingExceptions(exception)
            throw exception
        }
    }
}