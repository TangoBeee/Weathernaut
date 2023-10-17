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

    //Weather Music URLs
    const val CLOUD_MUSIC = "https://cdn.pixabay.com/download/audio/2021/11/21/audio_28c0ab46c1.mp3"
    const val ATMOSPHERIC_MUSIC = "https://cdn.pixabay.com/download/audio/2023/03/19/audio_1fa6409fc8.mp3"
    const val SUN_MUSIC = "https://cdn.pixabay.com/download/audio/2022/02/12/audio_8ca49a7f20.mp3"
    const val SNOW_MUSIC = "https://cdn.pixabay.com/download/audio/2021/11/23/audio_167b09f2b0.mp3"
    const val RAIN_MUSIC = "https://cdn.pixabay.com/download/audio/2022/07/06/audio_bafec9fdb3.mp3"

    //Base URLs
    const val OpenWeatherMap_API_BASE_URL = "https://api.openweathermap.org/"
    const val CURRENT_LOCATION_API_BASE_URL = "https://ipinfo.io/"

    const val CITY_LIMITS = 10

    //Request/Status Code
    const val COARSE_LOCATION_PERMISSION_REQUEST_CODE = 101
    const val NOTIFICATION_ID = 1
    const val CHANNEL_ID = "weather_music"
}