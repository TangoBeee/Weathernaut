package me.tangobee.weathernaut.model.weathermodel

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)