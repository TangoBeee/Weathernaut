package me.tangobee.weathernaut.data.remote

import me.tangobee.weathernaut.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyWeatherService {

    @GET("/v1/forecast")
    suspend fun getWeather(@Query("latitude") latitude: String, @Query("longitude") longitude: String, @Query("hourly") hourly: String, @Query("hourly") weatherCode: String, @Query("forecast_days") forecastDays: Int, @Query("timezone") timezone: String): Response<HourlyWeatherModel>

}