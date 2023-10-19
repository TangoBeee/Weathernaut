package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.remote.NextSevenDaysWeatherService
import me.tangobee.weathernaut.model.nextweathermodel.nextsevendays.NextSevenDaysWeatherModel

class NextSevenDaysWeatherRepository(private val service: NextSevenDaysWeatherService) {

    private val nextSevenDaysWeatherLiveData = MutableLiveData<NextSevenDaysWeatherModel>()

    val weatherLiveData: LiveData<NextSevenDaysWeatherModel>
        get() = nextSevenDaysWeatherLiveData

    suspend fun getWeather(latitude: String, longitude: String, dailyParameters: List<String>, timezone: String, forecastDays: Int) {
        val result = service.getWeather(latitude, longitude, dailyParameters, timezone, forecastDays)

        if(result.body() != null) {
            nextSevenDaysWeatherLiveData.value = result.body()
        }
    }

}