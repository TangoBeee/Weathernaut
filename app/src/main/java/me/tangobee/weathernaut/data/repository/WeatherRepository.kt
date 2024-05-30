package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.api.WeatherService
import me.tangobee.weathernaut.models.WeatherData.WeatherData

class WeatherRepository(private val weatherService: WeatherService) {

    private val weatherLiveData = MutableLiveData<WeatherData?>()

    val weatherData: LiveData<WeatherData?>
        get() = weatherLiveData

    suspend fun getWeather() {
        val weatherResult = weatherService.getWeather()

        if(weatherResult.isSuccessful && weatherResult.body() != null) {
            weatherLiveData.postValue(weatherResult.body())
        } else {
            weatherLiveData.postValue(null)
        }
    }

    fun updateWeatherData(weatherData: WeatherData) {
        weatherLiveData.postValue(weatherData)
    }

}