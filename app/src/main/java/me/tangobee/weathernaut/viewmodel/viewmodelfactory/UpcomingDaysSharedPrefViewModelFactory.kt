package me.tangobee.weathernaut.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.tangobee.weathernaut.data.repository.UpcomingDaysSharedPrefRepository
import me.tangobee.weathernaut.viewmodel.UpcomingDaysSharedPrefViewModel

class UpcomingDaysSharedPrefViewModelFactory(private val repository: UpcomingDaysSharedPrefRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpcomingDaysSharedPrefViewModel(repository) as T
    }

}