package me.tangobee.weathernaut.data.repository

import me.tangobee.weathernaut.data.local.UpcomingDaysSharedPrefService
import me.tangobee.weathernaut.model.nextweathermodel.nextsevendays.NextSevenDaysWeather

class UpcomingDaysSharedPrefRepository(private val service: UpcomingDaysSharedPrefService) {

    fun sendData(weatherData: NextSevenDaysWeather) {
        service.sendData(weatherData)
    }

    fun getData(): NextSevenDaysWeather? {
        return service.getData()
    }

}