package com.example.smartmessenger

import com.google.firebase.auth.FirebaseUser

interface AppSettings {

    fun getCurrentUId(): String?

    fun setCurrentUId(uId: String?)

}