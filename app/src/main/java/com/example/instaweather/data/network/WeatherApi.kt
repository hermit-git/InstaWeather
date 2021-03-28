package com.example.instaweather.data.network

import com.example.instaweather.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @QueryMap queries:Map<String,String>
    ):Response<CurrentWeather>




}