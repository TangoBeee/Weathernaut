package me.tangobee.weathernaut.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tangobee.weathernaut.data.remote.CurrentLocationService
import me.tangobee.weathernaut.model.CurrentLocationData

class CurrentLocationRepository(private val currentLocationService: CurrentLocationService) {

    private val approximateLocationLiveData = MutableLiveData<CurrentLocationData>()

    val locationLiveData: LiveData<CurrentLocationData>
        get() = approximateLocationLiveData

    suspend fun getLocation() {
        val result = currentLocationService.getApproximateLocation()

        if(result.body() != null) {
            approximateLocationLiveData.postValue(result.body())
        }
    }

}