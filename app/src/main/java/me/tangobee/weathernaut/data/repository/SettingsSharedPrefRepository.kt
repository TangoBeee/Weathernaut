package me.tangobee.weathernaut.data.repository

import me.tangobee.weathernaut.data.local.SettingsSharedPrefService
import me.tangobee.weathernaut.model.SettingsData

class SettingsSharedPrefRepository(private val settingsService: SettingsSharedPrefService) {

    fun getData(): SettingsData? {
        return settingsService.getData()
    }

    fun sendData(settingsData: SettingsData) {
        settingsService.setData(settingsData)
    }
}