package me.tangobee.weathernaut.util

import me.tangobee.weathernaut.R

class WeatherCodeToIcon {

    companion object {
        fun getWeatherIcon(weatherCode: Int): Int {
            return when (weatherCode) {
                0 -> R.drawable.icon_weather_sun
                1, 2, 3 -> R.drawable.icon_weather_cloud_sun
                45, 48 -> R.drawable.icon_weather_cloud_sun
                51, 53, 55 -> R.drawable.icon_weather_rain_cloud
                56, 57 -> R.drawable.icon_weather_rain_cloud
                61, 63, 65 -> R.drawable.icon_weather_rain_cloud
                66, 67 -> R.drawable.icon_weather_rain_cloud
                71, 73, 75 -> R.drawable.icon_weather_snow_cloud
                77 -> R.drawable.icon_weather_snow_cloud
                80, 81, 82 -> R.drawable.icon_weather_sun_rain_cloud
                85, 86 -> R.drawable.icon_weather_snow_cloud
                in 95..96 -> R.drawable.icon_weather_thunderstorm_cloud
                99 -> R.drawable.icon_weather_thunderstorm_cloud
                else -> R.drawable.icon_weather_sun
            }
        }
    }

}