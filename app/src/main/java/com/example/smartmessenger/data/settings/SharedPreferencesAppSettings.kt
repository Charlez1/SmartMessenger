package com.example.smartmessenger.data.settings

import android.content.Context
import com.example.smartmessenger.data.source.users.UserData
import com.google.android.gms.common.config.GservicesValue
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext appContext: Context
) : AppSettings {

    private val sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun setCurrentUId(uId: String?) {
        val editor = sharedPreferences.edit()
        if (uId == null)
            editor.remove(PREF_CURRENT_USER_ID)
        else
            editor.putString(PREF_CURRENT_USER_ID, uId)
        editor.apply()
    }

    override fun getCurrentUId(): String? =
        sharedPreferences.getString(PREF_CURRENT_USER_ID, null)

    override fun getCurrentUser(): UserData? {
        val userJson = sharedPreferences.getString(PREF_CURRENT_USER_DATA, null)
        return Gson().fromJson(userJson, UserData::class.java)
    }

    override fun setCurrentUser(userData: UserData?) {
        val editor = sharedPreferences.edit()
        if (userData == null)
            editor.remove(PREF_CURRENT_USER_DATA)
        else
            editor.putString(PREF_CURRENT_USER_DATA, Gson().toJson(userData)).apply()
        editor.apply()
    }

    override fun setIsRememberUser(isRememberUser: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREF_IS_REMEMBER_USER, isRememberUser)
        editor.apply()
    }

    override fun getIsRememberUser(): Boolean =
        sharedPreferences.getBoolean(PREF_IS_REMEMBER_USER, false)

    companion object {
        private const val PREF_CURRENT_USER_ID = "currentUid"
        private const val PREF_CURRENT_USER_DATA = "currentUser"
        private const val PREF_IS_REMEMBER_USER = "isRememberUser"
    }
}
