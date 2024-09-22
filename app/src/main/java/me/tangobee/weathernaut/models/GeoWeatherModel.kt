package me.tangobee.weathernaut.models

data class GeoWeatherModel(
    val ll: List<Double>,
    val city: String,
    val country: String,
    val timezone: String
)
