package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.SettingsSharedPrefRepository
import me.tangobee.weathernaut.model.SettingsData

class SettingsSharedPrefViewModel(private val settingsRepository: SettingsSharedPrefRepository): ViewModel() {

    fun getData(): SettingsData? {
        return settingsRepository.getData()
    }

    fun sendData(settingsData: SettingsData) {
        settingsRepository.sendData(settingsData)
    }
}