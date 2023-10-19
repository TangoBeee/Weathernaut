package me.tangobee.weathernaut.model.nextweathermodel.nextsevendays

data class NextSevenDaysWeather(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>
)