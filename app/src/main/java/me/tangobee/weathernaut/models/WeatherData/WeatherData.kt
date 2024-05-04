package me.tangobee.weathernaut.models.WeatherData

import me.tangobee.weathernaut.models.WeatherData.CurrentWeather.CurrentWeather
import me.tangobee.weathernaut.models.WeatherData.DailyWeather.DailyWeather
import me.tangobee.weathernaut.models.WeatherData.HourlyWeather.HourlyWeather

data class WeatherData(
    val city: String,
    val country: String,
    val current_weather: CurrentWeather,
    val daily_weather: DailyWeather,
    val hourly_weather: HourlyWeather
)