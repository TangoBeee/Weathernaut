package me.tangobee.weathernaut.constants

data class WeatherConstants(
    val code: Int,
    val description: String
)

object WeatherCodes {
    val weatherConstants = listOf(
        WeatherConstants(0, "Clear"),
        WeatherConstants(1, "Clear/Partly Cloudy"),
        WeatherConstants(2, "Partly Cloudy"),
        WeatherConstants(3, "Overcast"),
        WeatherConstants(45, "Fog"),
        WeatherConstants(48, "Rime Fog"),
        WeatherConstants(51, "Light Drizzle"),
        WeatherConstants(53, "Moderate Drizzle"),
        WeatherConstants(55, "Heavy Drizzle"),
        WeatherConstants(56, "Light Freezing Drizzle"),
        WeatherConstants(57, "Heavy Freezing Drizzle"),
        WeatherConstants(61, "Light Rain"),
        WeatherConstants(63, "Moderate Rain"),
        WeatherConstants(65, "Heavy Rain"),
        WeatherConstants(66, "Light Freezing Rain"),
        WeatherConstants(67, "Heavy Freezing Rain"),
        WeatherConstants(71, "Light Snow"),
        WeatherConstants(73, "Moderate Snow"),
        WeatherConstants(75, "Heavy Snow"),
        WeatherConstants(77, "Snow Grains"),
        WeatherConstants(80, "Light Rain Showers"),
        WeatherConstants(81, "Moderate Rain Showers"),
        WeatherConstants(82, "Heavy Rain Showers"),
        WeatherConstants(85, "Light Snow Showers"),
        WeatherConstants(86, "Heavy Snow Showers"),
        WeatherConstants(95, "Slight Thunderstorm"),
        WeatherConstants(96, "Slight Thunderstorm"),
        WeatherConstants(99, "Heavy Thunderstorm")
    )
}
