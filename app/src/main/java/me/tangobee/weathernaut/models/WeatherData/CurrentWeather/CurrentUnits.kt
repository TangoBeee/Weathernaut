package me.tangobee.weathernaut.models.WeatherData.CurrentWeather

data class CurrentUnits(
    val interval: String,
    var pressure_msl: String,
    val relative_humidity_2m: String,
    var temperature_2m: String,
    val time: String,
    val weather_code: String,
    var wind_speed_10m: String
)