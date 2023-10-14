package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.WeatherSharedPrefRepository
import me.tangobee.weathernaut.viewmodel.WeatherSharedPrefViewModel

class WeatherSharedPrefViewModelFactory(private val repository: WeatherSharedPrefRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherSharedPrefViewModel(repository) as T
    }

}