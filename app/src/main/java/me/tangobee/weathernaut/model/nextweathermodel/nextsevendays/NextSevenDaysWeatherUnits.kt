package me.tangobee.weathernaut.model.nextweathermodel.nextsevendays

data class NextSevenDaysWeatherUnits(
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val weathercode: String
)