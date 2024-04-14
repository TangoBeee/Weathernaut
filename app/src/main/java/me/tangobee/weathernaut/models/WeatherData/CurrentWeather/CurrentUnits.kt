package me.tangobee.weathernaut.models.WeatherData.CurrentWeather

data class CurrentUnits(
    val interval: String,
    val pressure_msl: String,
    val relative_humidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_speed_10m: String
)