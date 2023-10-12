package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.SettingsSharedPrefRepository
import me.tangobee.weathernaut.viewmodel.SettingsSharedPrefViewModel

class SettingsSharedPrefViewModelFactory(private val repository: SettingsSharedPrefRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsSharedPrefViewModel(repository) as T
    }

}