package com.example.instaweather.models


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("temp")
    val temp: Double,
)