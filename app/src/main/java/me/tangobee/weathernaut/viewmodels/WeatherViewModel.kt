package me.tangobee.weathernaut.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tangobee.weathernaut.data.repository.WeatherRepository
import me.tangobee.weathernaut.models.GeoWeatherModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    fun getWeather(coroutineExceptionHandler: CoroutineExceptionHandler) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            weatherRepository.getWeather()
        }
    }

    fun getGeoWeather(coroutineExceptionHandler: CoroutineExceptionHandler, geoWeatherModel: GeoWeatherModel) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            weatherRepository.getGeoWeather(geoWeatherModel)
        }
    }

    fun updateWeatherData(weatherData: WeatherData) {
        weatherRepository.updateWeatherData(weatherData)
    }

    val weatherLiveData : LiveData<WeatherData?>
        get() = weatherRepository.weatherData

}