package com.example.instaweather.services

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.instaweather.util.Constants.Companion.ACTION_START_SERVICE
import com.example.instaweather.util.Constants.Companion.ACTION_STOP_SERVICE
import com.example.instaweather.util.Constants.Companion.FASTEST_LOCATION_INTERVAL
import com.example.instaweather.util.Constants.Companion.LOCATION_UPDATE_INTERVAL
import com.example.instaweather.util.PermissionUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

class WeatherLocationService:LifecycleService() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isActive = MutableLiveData<Boolean>()
        val currentLocation = MutableLiveData<LatLng>()
    }

    private fun postInitialValue(){
        isActive.postValue(false)
        currentLocation.postValue(LatLng(0.0,0.0))
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isActive.observe(this,{
            updateLocation(it)
        })

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        postInitialValue()

        intent?.let {
            when(it.action){
                ACTION_START_SERVICE -> {
                    isActive.postValue(true)
                    Timber.d("Started Location service!")
                }
                ACTION_STOP_SERVICE -> {
                    isActive.postValue(false)
                    Timber.d("Stopped location service!")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isActive.value!!){
                result?.locations?.let { locations ->
                    for(location in locations){
                        Timber.d("current location : (${location.latitude},${location.longitude})")
                        postLocation(location)
                    }

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation(isActive: Boolean) {
        if(isActive){
            if(PermissionUtility.hasLocationPermission(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(request,locationCallback, Looper.getMainLooper())
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun postLocation(location:Location?){
        location?.let{
            val pos = LatLng(location.latitude,location.longitude)
            currentLocation.postValue(pos)

        }
    }



}