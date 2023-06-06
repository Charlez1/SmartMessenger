package com.example.smartmessenger.model.source.account

import com.google.firebase.database.FirebaseDatabase

class FirebaseAccountSource(
    private val database: FirebaseDatabase,
): AccountSource {

    override fun createAccount(uId: String, username: String, email: String) {


    }
}