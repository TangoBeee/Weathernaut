package me.tangobee.weathernaut.utils

import android.content.Context
import android.content.res.Resources
import java.util.Locale

object LanguageHelper {
    fun setLocale(context: Context, languageCode: String, resources: Resources) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun changeLanguage(sharedPreferencesHelper: SharedPreferencesHelper, language: String){
        sharedPreferencesHelper.saveLanguagePreference(language)
    }
}