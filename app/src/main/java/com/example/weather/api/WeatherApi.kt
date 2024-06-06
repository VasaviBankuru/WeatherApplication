package com.example.weather.api

import com.example.weather.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/current.json")
    suspend fun getCityWeather(
        @Query("Key") apiKey: String,
        @Query("q") city: String
    ): Response<WeatherModel>
}