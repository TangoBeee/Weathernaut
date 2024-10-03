package me.tangobee.weathernaut.utils

import java.util.Locale

class UnitConverter {

    fun convertTemperature(value: Double, fromUnit: String, toUnit: String): Double {
        val result = when (fromUnit to toUnit) {
            "celsius" to "fahrenheit" -> (value * 9 / 5) + 32
            "fahrenheit" to "celsius" -> (value - 32) * 5 / 9
            else -> value
        }

        return result.formatToTwoDecimalPlaces()
    }

    fun convertWindSpeed(value: Double, fromUnit: String, toUnit: String): Double {
        val result = when (fromUnit to toUnit) {
            "kmh" to "mph" -> value / 1.60934
            "mph" to "kmh" -> value * 1.60934
            "kmh" to "ms" -> value / 3.6
            "ms" to "kmh" -> value * 3.6
            "mph" to "ms" -> value * 0.44704
            "ms" to "mph" -> value / 0.44704
            else -> value
        }

        return result.formatToTwoDecimalPlaces()
    }

    fun convertPressure(value: Double, fromUnit: String, toUnit: String): Double {
        val result = when (fromUnit to toUnit) {
            "hpa" to "atm" -> value / 1013.25
            "atm" to "hpa" -> value * 1013.25
            else -> value
        }

        return result.formatToTwoDecimalPlaces()
    }

    private fun Double.formatToTwoDecimalPlaces(): Double {
        return String.format(Locale.US, "%.2f", this).toDouble()
    }
}
