package com.example.instaweather.models


import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
)