package com.example.smartmessenger

import com.example.smartmessenger.model.Field
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await

class FirebaseAccountsSource(
    private val auth: FirebaseAuth
): AccountSource {

    override suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password)//  .await()
        } catch (exception: Exception){
            when(exception) {
                is FirebaseTooManyRequestsException -> throw (TooManyRequestsException(exception.message ?: ""))
                is FirebaseAuthInvalidCredentialsException -> throw (InvalidUserException(exception.message ?: ""))
                is FirebaseNetworkException -> throw (ConnectionException(exception.message ?: ""))
                is FirebaseAuthWebException -> throw (BackendException(exception.message ?: ""))
                is FirebaseAuthException -> throw AuthException(exception.message ?: "")
                else -> throw UnknownException(exception.message ?: "")
            }
        }

    }

    override suspend fun getToken(): String? {
        return try {
            val user = auth.currentUser
            val tokenResult = user?.getIdToken(false)?.await()
            tokenResult?.token
        } catch (exception: Exception) {
            when(exception) {
                is FirebaseAuthRecentLoginRequiredException -> throw AuthRecentLoginRequiredException(exception.message ?: "")
                is FirebaseNetworkException -> throw (ConnectionException(exception.message ?: ""))
                else -> throw exception
            }
        }

    }

    override suspend fun signUp(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (exception: Exception) {
            when(exception) {
                is FirebaseAuthWeakPasswordException -> throw(AuthWeakPasswordException(exception.message ?: ""))
                is FirebaseAuthEmailException -> throw(AccountAlreadyExistsException(exception.message ?: ""))
                is FirebaseNetworkException -> throw(ConnectionException(exception.message ?: ""))
                is FirebaseAuthWebException -> throw(BackendException(exception.message ?: ""))
                is FirebaseAuthException -> throw AuthException(exception.message ?: "")
                else -> throw UnknownException(exception.message ?: "")
            }
        }

    }

    override suspend fun singInByToken(token: String) {
        try {
            auth.signInWithCustomToken(token)/// ???????????????? .await()
        } catch (exception: Exception){
            when(exception) {
                is FirebaseTooManyRequestsException -> throw (TooManyRequestsException(exception.message ?: ""))
                is FirebaseAuthInvalidCredentialsException -> throw (InvalidUserException(exception.message ?: ""))
                is FirebaseNetworkException -> throw (ConnectionException(exception.message ?: ""))
                is FirebaseAuthWebException -> throw (BackendException(exception.message ?: ""))
                is FirebaseAuthException -> throw AuthException(exception.message ?: "")
                else -> throw UnknownException(exception.message ?: "")
            }
        }
    }
}