package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.api.GeocodingService
import me.tangobee.weathernaut.models.GeocodingData.GeocodingModel
import me.tangobee.weathernaut.models.NewLocationModel

class GeocodingRepository(private val geocodingService: GeocodingService) {

    private val locationsLiveData = MutableLiveData<GeocodingModel?>()

    val locationsData: LiveData<GeocodingModel?>
        get() = locationsLiveData

    suspend fun getLocations(newLocationDataModel: NewLocationModel) {
        val locationResult = geocodingService.getLocations(newLocationDataModel)

        if(locationResult.isSuccessful && locationResult.body() != null) {
            locationsLiveData.postValue(locationResult.body())
        } else {
            locationsLiveData.postValue(null)
        }
    }

}