package me.tangobee.weathernaut.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import me.tangobee.weathernaut.models.SettingsModel

class SharedPreferencesHelper(context: Context) {
    companion object {
        private const val PREFS_NAME = "WeathernautPrefs"
        private const val SETTINGS_KEY = "SettingsKey"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveSettings(settings: SettingsModel) {
        val settingsJson = gson.toJson(settings)
        sharedPreferences.edit().putString(SETTINGS_KEY, settingsJson).apply()
    }

    fun getSettings(): SettingsModel? {
        val settingsJson = sharedPreferences.getString(SETTINGS_KEY, null)
        return if (settingsJson != null) {
            gson.fromJson(settingsJson, SettingsModel::class.java)
        } else {
            null
        }
    }

    fun updateSettings(updateFunction: (SettingsModel) -> SettingsModel) {
        val currentSettings = getSettings()
        if (currentSettings != null) {
            val updatedSettings = updateFunction.invoke(currentSettings)
            saveSettings(updatedSettings)
        }
    }
}

