package me.tangobee.weathernaut.utils

import me.tangobee.weathernaut.constants.UnitsMapper
import me.tangobee.weathernaut.models.SettingsModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData

class WeatherHelper(private val settings: SettingsModel, private val weatherData: WeatherData) {

    private val unitConverter = UnitConverter()

    fun convertWeatherData(): WeatherData {
        weatherData.current_weather.current.temperature_2m = convertTemperature(
            weatherData.current_weather.current.temperature_2m,
            weatherData.current_weather.current_units.temperature_2m,
            settings.tempUnit
        )
        weatherData.current_weather.current_units.temperature_2m = UnitsMapper.getShorthandUnit(settings.tempUnit)

        weatherData.current_weather.current.wind_speed_10m = convertWindSpeed(
            weatherData.current_weather.current.wind_speed_10m,
            weatherData.current_weather.current_units.wind_speed_10m,
            settings.windSpeedUnit
        )
        weatherData.current_weather.current_units.wind_speed_10m = UnitsMapper.getShorthandUnit(settings.windSpeedUnit)

        weatherData.current_weather.current.pressure_msl = convertPressure(
            weatherData.current_weather.current.pressure_msl,
            weatherData.current_weather.current_units.pressure_msl,
            settings.pressureUnit
        )
        weatherData.current_weather.current_units.pressure_msl = UnitsMapper.getShorthandUnit(settings.pressureUnit)

        weatherData.hourly_weather.hourly.temperature_2m = weatherData.hourly_weather.hourly.temperature_2m.map {
            convertTemperature(it, weatherData.hourly_weather.hourly_units.temperature_2m, settings.tempUnit)
        }
        weatherData.hourly_weather.hourly_units.temperature_2m = UnitsMapper.getShorthandUnit(settings.tempUnit)

        weatherData.daily_weather.daily.temperature_2m_max = weatherData.daily_weather.daily.temperature_2m_max.map {
            convertTemperature(it, weatherData.daily_weather.daily_units.temperature_2m_max, settings.tempUnit)
        }
        weatherData.daily_weather.daily_units.temperature_2m_max = UnitsMapper.getShorthandUnit(settings.tempUnit)

        weatherData.daily_weather.daily.temperature_2m_min = weatherData.daily_weather.daily.temperature_2m_min.map {
            convertTemperature(it, weatherData.daily_weather.daily_units.temperature_2m_min, settings.tempUnit)
        }
        weatherData.daily_weather.daily_units.temperature_2m_min = UnitsMapper.getShorthandUnit(settings.tempUnit)

        return weatherData
    }

    private fun convertTemperature(value: Double, fromUnit: String, toUnit: String): Double {
        val fromUnitName = unitName(fromUnit)
        val toUnitName = unitName(toUnit)
        return unitConverter.convertTemperature(value, fromUnitName, toUnitName)
    }

    private fun convertWindSpeed(value: Double, fromUnit: String, toUnit: String): Double {
        val fromUnitName = unitName(fromUnit)
        val toUnitName = unitName(toUnit)
        return unitConverter.convertWindSpeed(value, fromUnitName, toUnitName)
    }

    private fun convertPressure(value: Double, fromUnit: String, toUnit: String): Double {
        val fromUnitName = unitName(fromUnit)
        val toUnitName = unitName(toUnit)
        return unitConverter.convertPressure(value, fromUnitName, toUnitName)
    }

    private fun unitName(unit: String): String {
        return when (unit) {
            "°C" -> "celsius"
            "°F" -> "fahrenheit"
            "km/h" -> "kmh"
            "mph" -> "mph"
            "m/s" -> "ms"
            "hPa" -> "hpa"
            "atm" -> "atm"
            else -> unit
        }
    }
}
