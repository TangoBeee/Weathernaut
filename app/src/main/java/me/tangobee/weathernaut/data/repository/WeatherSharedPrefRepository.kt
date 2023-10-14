package me.tangobee.weathernaut.data.repository

import me.tangobee.weathernaut.data.local.WeatherSharedPrefService
import me.tangobee.weathernaut.model.weathermodel.WeatherData

class WeatherSharedPrefRepository(private val weatherService: WeatherSharedPrefService) {

    fun getData(): WeatherData? {
        return weatherService.getData()
    }

    fun sendData(weatherData: WeatherData) {
        weatherService.sendData(weatherData)
    }

}