package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.LocationSharedPrefRepository
import me.tangobee.weathernaut.viewmodel.LocationSharedPrefViewModel

class LocationSharedPrefViewModelFactory(private val repository: LocationSharedPrefRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationSharedPrefViewModel(repository) as T
    }

}