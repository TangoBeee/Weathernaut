package me.tangobee.weathernaut.util

object AppConstants {
    //Permissions
    const val COARSE_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_COARSE_LOCATION

    //SharedPref name
    const val SHARED_PREF_NAME = "weathernautData"

    //SharedPref data name
    const val LOCATION_SHARED_PREF = "locationData"
    const val WEATHER_SHARED_PREF = "weatherData"
    const val SETTINGS_SHARED_PREF = "settingsData"

    //Base URLs
    const val OpenWeatherMap_API_BASE_URL = "https://api.openweathermap.org/"
    const val CURRENT_LOCATION_API_BASE_URL = "https://ipinfo.io/"

    const val CITY_LIMITS = 10

    //Request/Status Code
    const val COARSE_LOCATION_PERMISSION_REQUEST_CODE = 101
}