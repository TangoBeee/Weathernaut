package me.tangobee.weathernaut.data.remote

import me.tangobee.weathernaut.model.weathermodel.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appId: String): Response<WeatherData>

}