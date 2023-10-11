package me.tangobee.weathernaut.data.remote

import me.tangobee.weathernaut.model.CurrentLocationData
import retrofit2.Response
import retrofit2.http.GET

interface CurrentLocationService {

    @GET("/json")
    suspend fun getApproximateLocation(): Response<CurrentLocationData>

}