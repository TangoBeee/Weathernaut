package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.NextSevenDaysWeatherRepository
import me.tangobee.weathernaut.model.nextweathermodel.nextsevendays.NextSevenDaysWeatherModel

class NextSevenDaysWeatherViewModel(private val repository: NextSevenDaysWeatherRepository) : ViewModel() {

    suspend fun getWeather(latitude: String, longitude: String, dailyParameters: List<String>, timezone: String, forecastDays: Int) {
        repository.getWeather(latitude, longitude, dailyParameters, timezone, forecastDays)
    }

    val weatherLiveData: LiveData<NextSevenDaysWeatherModel>
        get() = repository.weatherLiveData

}