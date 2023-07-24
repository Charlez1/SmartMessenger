package com.example.smartmessenger.model.source.users

import com.example.smartmessenger.AccountAlreadyExistsException
import com.example.smartmessenger.ConnectionException
import com.example.smartmessenger.UnknownException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class FirestoreUsersSource(
    private val database: FirebaseFirestore
): UsersSource {

    override suspend fun createAccount(uId: String, username: String, email: String) {
        try {
            val userReference = database.collection("users").document(uId)
            val userData = hashMapOf(
                "email" to email,
                "username" to username)
            userReference.set(userData).await()
        } catch (e: FirebaseFirestoreException) {
            processingRemainingExceptions(e)
        }
    }

    override suspend fun getUserData(uId: String): UserData {
        val userReference = database.collection("users").document(uId).get().await()
        return UserData(
            username = userReference.getString("name") ?: "",
            email = userReference.getString("email") ?: ""
        )

    }


    private fun processingRemainingExceptions(exception: FirebaseFirestoreException) {
        when (exception.code) {
            FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(exception.message ?: "")
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> throw AccountAlreadyExistsException(exception.message ?: "")
            else -> throw UnknownException(exception.message ?: "")
        }
    }
}