package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.HourlyWeatherRepository
import me.tangobee.weathernaut.model.nextweathermodel.hourlyweathers.HourlyWeatherModel

class HourlyWeatherViewModel(private val repository: HourlyWeatherRepository) : ViewModel() {

    suspend fun getWeather(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String) {
        repository.getWeather(lat, lon ,hourly, weatherCode, forecastDays, timezone)
    }

    val weatherLiveData: LiveData<HourlyWeatherModel>
        get() = repository.weatherLiveData

}