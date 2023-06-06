package com.example.smartmessenger

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

    companion object {
        private const val PREF_CURRENT_USER_ID = "currentUid"
    }
}
