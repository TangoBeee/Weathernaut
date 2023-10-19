package me.tangobee.weathernaut.model.nextweathermodel.nextsevendays

data class NextSevenDaysWeatherModel(
    val daily: NextSevenDaysWeather,
    val daily_units: NextSevenDaysWeatherUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)