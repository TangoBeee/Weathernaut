package me.tangobee.weathernaut.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.GeocodingRepository

class GeocodingViewModelFactory(private val geocodingRepository: GeocodingRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GeocodingViewModel(geocodingRepository) as T
    }

}