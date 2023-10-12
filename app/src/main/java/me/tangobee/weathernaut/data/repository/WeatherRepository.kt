package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.remote.WeatherService
import me.tangobee.weathernaut.model.weathermodel.WeatherData

class WeatherRepository(private val weatherService: WeatherService) {

    private val weatherLiveData = MutableLiveData<WeatherData>()

    val weatherData: LiveData<WeatherData>
        get() = weatherLiveData

    suspend fun getWeather(lat: String, lon: String, appId: String) {
        val result = weatherService.getWeather(lat, lon, appId)

        if(result.body() != null) {
            weatherLiveData.postValue(result.body())
        }
    }

}