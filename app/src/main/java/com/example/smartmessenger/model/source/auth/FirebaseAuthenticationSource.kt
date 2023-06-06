package com.example.smartmessenger.model.source.auth

import com.example.smartmessenger.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationSource(
    private val auth: FirebaseAuth
): AuthenticationSource {

    override suspend fun signIn(email: String, password: String, setCurrentUId: (String?) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val currentUId = getCurrentUId()
            setCurrentUId(currentUId)
        } catch (exception: Exception){
            when(exception) {
                is FirebaseTooManyRequestsException -> throw (TooManyRequestsException(exception.message ?: ""))
                is FirebaseAuthInvalidCredentialsException -> throw (InvalidUserException(exception.message ?: ""))
                else -> processingRemainingExceptions(exception)
            }
        }
    }

    override suspend fun signUp(username: String, email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (exception: Exception) {
            when(exception) {
                is FirebaseAuthWeakPasswordException -> throw(AuthWeakPasswordException(exception.message ?: ""))
                is FirebaseAuthEmailException -> throw(AccountAlreadyExistsException(exception.message ?: ""))
                else -> processingRemainingExceptions(exception)
            }
        }
    }

    private fun processingRemainingExceptions(exception: Exception) {
        when(exception) {
            is FirebaseNetworkException -> throw(ConnectionException(exception.message ?: ""))
            is FirebaseAuthWebException -> throw (BackendException(exception.message ?: ""))
            is FirebaseAuthException -> throw AuthException(exception.message ?: "")
            else -> throw UnknownException(exception.message ?: "")
        }
    }

    override fun getCurrentUId(): String {
        try {
            val user = auth.currentUser
            return user?.uid.toString()
        } catch (exception: Exception) {
            processingRemainingExceptions(exception)
            throw exception
        }
    }
}