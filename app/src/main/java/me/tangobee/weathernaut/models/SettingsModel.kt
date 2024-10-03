package me.tangobee.weathernaut.models

data class SettingsModel(
    var tempUnit: String = "celsius",
    var windSpeedUnit: String = "kmh",
    var pressureUnit: String = "hpa",
    var isMusicOn: Boolean = true,
    var Language: String= "",
    var LanguageName: String=""
)