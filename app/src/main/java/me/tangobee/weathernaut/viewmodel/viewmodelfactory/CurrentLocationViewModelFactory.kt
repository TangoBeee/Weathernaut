package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.CurrentLocationRepository
import me.tangobee.weathernaut.viewmodel.CurrentLocationViewModel

class CurrentLocationViewModelFactory(private val repository: CurrentLocationRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentLocationViewModel(repository) as T
    }

}