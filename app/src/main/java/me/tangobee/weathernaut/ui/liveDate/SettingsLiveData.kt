package me.tangobee.weathernaut.ui.liveDate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.model.SettingsData

object SettingsLiveData {
    private val settingsLiveData = MutableLiveData<SettingsData>()

    fun getSettingsLiveData(): LiveData<SettingsData> = settingsLiveData

    fun updateSettingsData (newSettingsData: SettingsData) {
        settingsLiveData.value = newSettingsData
    }
}
