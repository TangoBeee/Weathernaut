package me.tangobee.weathernaut.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tangobee.weathernaut.data.repository.GeocodingRepository
import me.tangobee.weathernaut.models.GeocodingData.GeocodingModel
import me.tangobee.weathernaut.models.NewLocationModel

class GeocodingViewModel(private val geocodingRepository: GeocodingRepository) : ViewModel() {

    fun getLocations(newLocationDataModel: NewLocationModel, coroutineExceptionHandler: CoroutineExceptionHandler) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            geocodingRepository.getLocations(newLocationDataModel)
        }
    }

    val locationLiveData : LiveData<GeocodingModel?>
        get() = geocodingRepository.locationsData

}