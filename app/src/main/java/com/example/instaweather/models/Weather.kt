package com.example.instaweather.models


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("main")
    val description: String
)