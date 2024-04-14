package me.tangobee.weathernaut.data.api

import me.tangobee.weathernaut.models.WeatherData.WeatherData
import retrofit2.Response
import retrofit2.http.POST

interface WeatherService {

    @POST("/api/weather")
    suspend fun getWeather() : Response<WeatherData>

}