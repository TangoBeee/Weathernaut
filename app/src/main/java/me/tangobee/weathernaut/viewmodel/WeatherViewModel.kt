package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.WeatherRepository
import me.tangobee.weathernaut.model.weathermodel.WeatherData

class WeatherViewModel(private val weatherRepository: WeatherRepository): ViewModel() {

    suspend fun getWeather(lat: String, lon: String, appId: String) {
        weatherRepository.getWeather(lat, lon, appId)
    }

    val weatherLiveData: LiveData<WeatherData>
        get() = weatherRepository.weatherData

}