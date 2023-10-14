package me.tangobee.weathernaut.model.weathermodel

data class WeatherType(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)