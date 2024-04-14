package me.tangobee.weathernaut.models.WeatherData.CurrentWeather

data class Current(
    val interval: Int,
    val pressure_msl: Double,
    val relative_humidity_2m: Int,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_speed_10m: Double
)