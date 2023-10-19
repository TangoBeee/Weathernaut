package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.remote.HourlyWeatherService
import me.tangobee.weathernaut.model.nextweathermodel.hourlyweathers.HourlyWeatherModel

class HourlyWeatherRepository(private val service: HourlyWeatherService) {

    private val hourlyWeatherLiveData = MutableLiveData<HourlyWeatherModel>()

    val weatherLiveData: LiveData<HourlyWeatherModel>
        get() = hourlyWeatherLiveData

    suspend fun getWeather(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String) {
        val result = service.getWeather(lat, lon, hourly, weatherCode, forecastDays, timezone)

        if(result.body() != null) {
            hourlyWeatherLiveData.value = result.body()
        }
    }

}