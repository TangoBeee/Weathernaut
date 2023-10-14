package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.WeatherSharedPrefRepository
import me.tangobee.weathernaut.model.weathermodel.WeatherData

class WeatherSharedPrefViewModel(private val weatherRepository: WeatherSharedPrefRepository): ViewModel() {

    fun getData(): WeatherData? {
        return weatherRepository.getData()
    }

    fun sendData(weatherData: WeatherData) {
        weatherRepository.sendData(weatherData)
    }

}