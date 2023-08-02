package com.example.smartmessenger.model.settings

import com.example.smartmessenger.model.source.users.UserData
import com.google.firebase.auth.FirebaseUser

interface AppSettings {

    fun getCurrentUId(): String?

    fun setCurrentUId(uId: String?)

    fun getCurrentUser(): UserData?

    fun setCurrentUser(userData: UserData?)

    fun setIsRememberUser(isRememberUser: Boolean)

    fun getIsRememberUser(): Boolean
}