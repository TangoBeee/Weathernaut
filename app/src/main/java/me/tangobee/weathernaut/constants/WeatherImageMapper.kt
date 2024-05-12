package me.tangobee.weathernaut.constants

import me.tangobee.weathernaut.R

class WeatherImageMapper {
    companion object {
        fun getImageForWeatherCode(weatherCode: Int): Int {
            return when (weatherCode) {
                0, 1 -> R.drawable.icon_weather_sun
                2, 3 -> R.drawable.icon_weather_sun_cloud
                45, 48 -> R.drawable.icon_weather_cloud_fog
                in 51..55, in 56..57 -> R.drawable.icon_weather_rain_cloud
                in 61..65, in 66..67 -> R.drawable.icon_weather_rain_cloud
                71, 73, 75, 77 -> R.drawable.icon_weather_snow_cloud
                80, 81, 82 -> R.drawable.icon_weather_sun_cloud_rain
                85, 86 -> R.drawable.icon_weather_snow_cloud
                95, 96, 99 -> R.drawable.icon_weather_thunderstorm_cloud
                else -> R.drawable.icon_weather_cloud
            }
        }
    }
}
