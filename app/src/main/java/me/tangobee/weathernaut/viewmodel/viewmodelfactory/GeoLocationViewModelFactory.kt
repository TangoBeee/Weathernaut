package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.GeoLocationRepository
import me.tangobee.weathernaut.viewmodel.GeoLocationViewModel

class GeoLocationViewModelFactory(private val repository: GeoLocationRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GeoLocationViewModel(repository) as T
    }
}