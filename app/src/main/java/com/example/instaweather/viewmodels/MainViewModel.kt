package com.example.instaweather.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.instaweather.data.Repository
import com.example.instaweather.models.CurrentWeather
import com.example.instaweather.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
):AndroidViewModel(application) {

    // Retrofit
    val currentWeatherResponse:MutableLiveData<NetworkResult<CurrentWeather>> = MutableLiveData()

    fun getCurrentWeather(queries:Map<String,String>) = viewModelScope.launch {
        getCurrentWeatherSafeCall(queries)
    }

    private suspend fun getCurrentWeatherSafeCall(queries: Map<String, String>) {
        currentWeatherResponse.value = NetworkResult.Loading()
        if(hasNetworkConnection()){
            try{
                val response = repository.remote.getCurrentWeather(queries)
                currentWeatherResponse.value = handleCurrentWeatherResponse(response)

            } catch (e:Exception){
                currentWeatherResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            currentWeatherResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleCurrentWeatherResponse(response: Response<CurrentWeather>): NetworkResult<CurrentWeather>? {
        return when{
            response.message().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("Api Key Limited")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message().toString())
            }
        }
    }

    private fun hasNetworkConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }


}