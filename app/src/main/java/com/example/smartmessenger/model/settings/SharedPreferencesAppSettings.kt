package com.example.smartmessenger.model.settings

import android.content.Context

class SharedPreferencesAppSettings(
    appContext: Context
) : AppSettings {

    private val sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun setCurrentUId(UId: String?) {
        val editor = sharedPreferences.edit()
        if (UId == null)
            editor.remove(PREF_CURRENT_USER_ID)
        else
            editor.putString(PREF_CURRENT_USER_ID, UId)
        editor.apply()
    }

    override fun getCurrentUId(): String? =
        sharedPreferences.getString(PREF_CURRENT_USER_ID, null)

    override fun setIsRememberUser(isRememberUser: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREF_IS_REMEMBER_USER, isRememberUser)
        editor.apply()
    }

    override fun getIsRememberUser(): Boolean =
        sharedPreferences.getBoolean(PREF_IS_REMEMBER_USER, false)

    companion object {
        private const val PREF_CURRENT_USER_ID = "currentUid"
        private const val PREF_IS_REMEMBER_USER = "isRememberUser"
    }
}
