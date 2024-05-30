package me.tangobee.weathernaut.constants

object UnitsMapper {
    private val unitMap: Map<String, String> = mapOf(
        "°C" to "celsius",
        "°F" to "fahrenheit",
        "km/h" to "kmh",
        "mph" to "mph",
        "m/s" to "ms",
        "hPa" to "hpa",
        "atm" to "atm"
    )

    private val reverseUnitMap: Map<String, String> = unitMap.entries.associate { (key, value) -> value to key }

    fun getFullUnit(shorthand: String): String {
        return unitMap.getOrDefault(shorthand, shorthand)
    }

    fun getShorthandUnit(fullUnit: String): String {
        return reverseUnitMap.getOrDefault(fullUnit, fullUnit)
    }
}
