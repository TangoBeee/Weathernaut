package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.HourlyWeatherRepository
import me.tangobee.weathernaut.viewmodel.HourlyWeatherViewModel

class HourlyWeatherViewModelFactory(private val repository: HourlyWeatherRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HourlyWeatherViewModel(repository) as T
    }

}