package com.example.smartmessenger.data.settings

import com.example.smartmessenger.data.source.users.UserData
import com.google.firebase.auth.FirebaseUser

interface AppSettings {

    fun getCurrentUId(): String?

    fun setCurrentUId(uId: String?)

    fun getCurrentUser(): UserData?

    fun setCurrentUser(userData: UserData?)

    fun setIsRememberUser(isRememberUser: Boolean)

    fun getIsRememberUser(): Boolean
}