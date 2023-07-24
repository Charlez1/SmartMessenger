package com.example.smartmessenger.model.settings

import com.google.firebase.auth.FirebaseUser

interface AppSettings {

    fun getCurrentUId(): String?

    fun setCurrentUId(uId: String?)

    fun setIsRememberUser(isRememberUser: Boolean)

    fun getIsRememberUser(): Boolean
}