package me.tangobee.weathernaut.models.WeatherData.HourlyWeather

data class Hourly(
    var temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>
)