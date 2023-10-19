package me.tangobee.weathernaut.data.remote

import me.tangobee.weathernaut.model.nextweathermodel.nextsevendays.NextSevenDaysWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NextSevenDaysWeatherService {

    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") dailyParameters: List<String>,
        @Query("timezone") timezone: String,
        @Query("forecast_days") forecastDays: Int
    ): Response<NextSevenDaysWeatherModel>

}