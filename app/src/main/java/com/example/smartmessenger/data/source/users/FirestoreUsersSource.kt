package com.example.smartmessenger.data.source.users

import com.example.smartmessenger.utils.AccountAlreadyExistsException
import com.example.smartmessenger.utils.ConnectionException
import com.example.smartmessenger.utils.UnknownException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUsersSource @Inject constructor(
    private val database: FirebaseFirestore
): UsersSource {

    override suspend fun createAccount(uId: String, userData: UserData) {
        saveFirebaseExecute {
            val userReference = database.collection("users").document(uId)
            val userDataHashMap = hashMapOf(
                "email" to userData.email,
                "username" to userData.username,
                "unique_name" to userData.uniqueName)
            userReference.set(userDataHashMap).await()
        }
    }

    override suspend fun getUserData(uId: String): UserData {
        return saveFirebaseExecute {
            val userReference = database.collection("users").document(uId)
            val userSnapshot = userReference.get().await()
            UserData(
                email = userSnapshot.getString("email") ?: "",
                username = userSnapshot.getString("name") ?: "",
                uniqueName = userSnapshot.getString("unique_name") ?: ""
            )
        }

    }

    override suspend fun checkUniqueName(uniqueName: String): Boolean {
        val userReference = database.collection("users")

        return saveFirebaseExecute {
            val querySnapshot = userReference.whereEqualTo("username", uniqueName).get().await()
            querySnapshot.documents.isEmpty()
        }
    }

    override suspend fun deleteUser(uId: String) {
        saveFirebaseExecute {
            val userReference = database.collection("users").document(uId)
            userReference.delete().await()
        }
    }

    override suspend fun getUserDataByUniqueName(uniqueName: String): UserData {
        val userReference = database.collection("users")
            .whereEqualTo("unique_name", uniqueName)

        return saveFirebaseExecute {
            val snapshot = userReference.get().await()
            val document = snapshot.documents.first()
            UserData(
                email = document.getString("email") ?: "",
                username = document.getString("name") ?: "",
                uniqueName = document.getString("unique_name") ?: ""
            )
        }
    }


    private suspend fun <T> saveFirebaseExecute(block: suspend () -> T) : T {
        return try {
            block()
        } catch (exception: FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE -> throw ConnectionException(exception.message ?: "")
                FirebaseFirestoreException.Code.ALREADY_EXISTS -> throw AccountAlreadyExistsException(exception.message ?: "")
                else -> throw UnknownException(exception.message ?: "")
            }
        }
    }
}