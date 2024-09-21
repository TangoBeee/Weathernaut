package me.tangobee.weathernaut.data.api

import me.tangobee.weathernaut.models.GeocodingData.GeocodingModel
import me.tangobee.weathernaut.models.NewLocationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GeocodingService {

    @POST("/api/geocoding")
    suspend fun getLocations(@Body newLocationDataModel: NewLocationModel) : Response<GeocodingModel>

}