package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.NextSevenDaysWeatherRepository
import me.tangobee.weathernaut.viewmodel.NextSevenDaysWeatherViewModel

class NextSevenDaysWeatherViewModelFactory(private val repository: NextSevenDaysWeatherRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NextSevenDaysWeatherViewModel(repository) as T
    }

}