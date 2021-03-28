package com.example.instaweather.util

class Constants {

    companion object{
        const val API_KEY = "3e6f7fd4dc19497672675594dc4ec066"
        const val BASE_URL = "http://api.openweathermap.org/"
        const val DEFAULT_UNIT = "metric"
        const val API_KEY_PARAMETER = "appid"
        const val UNITS_PARAMETER = "units"
        const val LATITUDE_PARAMETER = "lat"
        const val LONGITUDE_PARAMETER = "lon"

        const val PERMISSION_REQUEST_CODE = 0

        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

        const val LOCATION_UPDATE_INTERVAL = 5000L
        const val FASTEST_LOCATION_INTERVAL = 2000L


        // Units
        const val TEMPERATUTE_UNIT = "℃"
    }
}