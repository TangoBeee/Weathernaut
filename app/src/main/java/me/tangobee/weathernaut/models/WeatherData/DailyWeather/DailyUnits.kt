package me.tangobee.weathernaut.models.WeatherData.DailyWeather

data class DailyUnits(
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val weather_code: String
)