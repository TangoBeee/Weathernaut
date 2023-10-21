package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.UpcomingDaysSharedPrefRepository
import me.tangobee.weathernaut.model.nextweathermodel.nextsevendays.NextSevenDaysWeather

class UpcomingDaysSharedPrefViewModel(private val repository: UpcomingDaysSharedPrefRepository): ViewModel() {

    fun getData(): NextSevenDaysWeather? {
        return repository.getData()
    }

    fun sendData(weatherData: NextSevenDaysWeather) {
        repository.sendData(weatherData)
    }

}