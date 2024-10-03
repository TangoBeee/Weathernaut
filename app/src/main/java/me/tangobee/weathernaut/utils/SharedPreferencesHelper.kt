package me.tangobee.weathernaut.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import me.tangobee.weathernaut.models.GeoWeatherModel
import me.tangobee.weathernaut.models.SettingsModel

class SharedPreferencesHelper(context: Context) {
    companion object {
        private const val PREFS_NAME = "WeathernautPrefs"
        private const val SETTINGS_KEY = "SettingsKey"
        private const val GEOCODING_KEY = "GeocodingKey"
        private const val LANGUAGE_PREF = "LanguagePreference"
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

    fun saveGeocodingData(geoWeatherModel: GeoWeatherModel) {
        val geocodingJson = gson.toJson(geoWeatherModel)
        sharedPreferences.edit().putString(GEOCODING_KEY, geocodingJson).apply()
    }

    fun getGeocodingData(): GeoWeatherModel? {
        val geocodingJson = sharedPreferences.getString(GEOCODING_KEY, null)
        return if (geocodingJson != null) {
            gson.fromJson(geocodingJson, GeoWeatherModel::class.java)
        } else {
            null
        }
    }

    fun saveLanguagePreference(language: String) {
        val languageJson = gson.toJson(language)
        sharedPreferences.edit().putString(LANGUAGE_PREF, languageJson).apply()
    }

    fun getLanguagePreference(): String {
        val languageJson = sharedPreferences.getString(LANGUAGE_PREF, null)
        return if(languageJson != null){
            gson.fromJson(languageJson, String::class.java)
        } else{
            "en"
        }
    }
}

