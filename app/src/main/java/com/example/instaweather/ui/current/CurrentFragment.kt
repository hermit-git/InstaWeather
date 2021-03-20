package com.example.instaweather.ui.current

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.instaweather.R
import com.example.instaweather.databinding.FragmentCurrentBinding
import com.example.instaweather.models.CurrentWeather
import com.example.instaweather.util.Constants.Companion.API_KEY
import com.example.instaweather.util.Constants.Companion.API_KEY_PARAMETER
import com.example.instaweather.util.Constants.Companion.DEFAULT_UNIT
import com.example.instaweather.util.Constants.Companion.LATITUDE_PARAMETER
import com.example.instaweather.util.Constants.Companion.LONGITUDE_PARAMETER
import com.example.instaweather.util.Constants.Companion.TEMPERATUTE_UNIT
import com.example.instaweather.util.Constants.Companion.UNITS_PARAMETER
import com.example.instaweather.util.NetworkResult
import com.example.instaweather.viewmodels.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.jar.Manifest


class CurrentFragment : Fragment() {

    private  var _binding:FragmentCurrentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel:MainViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBinding.inflate(inflater,container,false)

        lifecycleScope.launch {
            mainViewModel.getCurrentWeather(applyCurrentQueries())
            mainViewModel.currentWeatherResponse.observe(viewLifecycleOwner,{ response->
                when(response){
                    is NetworkResult.Success -> {
                        response.data?.let {
                            setUpUi(it)
                        }
                    }
                    else -> {
                        //
                    }
                }

            })
        }

        return binding.root
    }

    private fun setUpUi(currentWeather: CurrentWeather) {
        binding.apply {
            locationTv.text = currentWeather.name
            tempTv.text = currentWeather.main.temp.toString()
            descriptionTv.text = currentWeather.weather[0].description
            humidityTextView.text = currentWeather.main.humidity.toString()
            pressureTextView.text = currentWeather.main.pressure.toString()
            windSpeedTextView.text = currentWeather.wind.speed.toString()
        }


    }

    private fun applyCurrentQueries(): Map<String, String> {
        val queries = HashMap<String,String>()
        queries[API_KEY_PARAMETER] = API_KEY
        queries[UNITS_PARAMETER] = DEFAULT_UNIT

        val location = getUserLocation()

        queries[LATITUDE_PARAMETER] = location.latitude.toString()
        queries[LONGITUDE_PARAMETER] = location.longitude.toString()

        return queries
    }

    private fun getUserLocation(): UserLocation {
        var latitude = 23.5204
        var longitude = 87.3119
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1)
        }  else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if(location == null){
                    showSnackBar("Sorry can't get location!")
                } else {
                    Log.d("Location","$location")
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }
        return UserLocation(latitude,longitude)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    showSnackBar("Location Permission Granted!")
                }else {
                    showSnackBar("Permission Denied!")
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }
}