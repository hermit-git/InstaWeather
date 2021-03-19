package com.example.instaweather.data

import com.example.instaweather.data.network.WeatherApi
import com.example.instaweather.models.CurrentWeather
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getCurrentWeather(queries:Map<String,String>):Response<CurrentWeather>{
        return weatherApi.getCurrentWeather(queries)
    }
}