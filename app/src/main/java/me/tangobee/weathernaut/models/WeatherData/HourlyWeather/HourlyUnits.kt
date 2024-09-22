package me.tangobee.weathernaut.models.WeatherData.HourlyWeather

data class HourlyUnits(
    var temperature_2m: String,
    val time: String,
    val weather_code: String
)