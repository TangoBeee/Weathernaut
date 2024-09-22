package me.tangobee.weathernaut

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import me.tangobee.weathernaut.data.RetrofitHelper
import me.tangobee.weathernaut.data.api.WeatherService
import me.tangobee.weathernaut.data.repository.WeatherRepository
import me.tangobee.weathernaut.databinding.ActivityMainBinding
import me.tangobee.weathernaut.models.WeatherData.WeatherData
import me.tangobee.weathernaut.services.WeatherMusicService
import me.tangobee.weathernaut.utils.SharedPreferencesHelper
import me.tangobee.weathernaut.utils.WeatherHelper
import me.tangobee.weathernaut.viewmodels.WeatherViewModel
import me.tangobee.weathernaut.viewmodels.WeatherViewModelFactory
import java.net.UnknownHostException
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private var settingsUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val noInternetLiveData : MutableLiveData<Boolean> =  MutableLiveData(false)

        coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            if(throwable is UnknownHostException) {
                noInternetLiveData.postValue(true)
            }
        }

        noInternetLiveData.observe(this) {noInternet ->
            if(noInternet) {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                Thread.sleep(1000)
                exitProcess(0)
            }
        }

        val settingsModel = SharedPreferencesHelper(this).getSettings()
        if(settingsModel?.isMusicOn != false) {
            val startMusicIntent = Intent(this, WeatherMusicService::class.java)
            startService(startMusicIntent)
        }

        fetchData()
        splashScreen.setKeepOnScreenCondition { (weatherViewModel.weatherLiveData.value == null) }

        setContentView(binding.root)
    }

    private fun fetchData() {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(weatherRepository))[WeatherViewModel::class.java]

        val geocodingData = sharedPreferencesHelper.getGeocodingData()

        if(weatherViewModel.weatherLiveData.value == null) {
            if(geocodingData == null) {
                weatherViewModel.getWeather(coroutineExceptionHandler)
            } else {
                weatherViewModel.getGeoWeather(coroutineExceptionHandler, geocodingData)
            }
        }

        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            if(weatherData == null) {
                Toast.makeText(this, getString(R.string.api_fetching_error), Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
                exitProcess(0)
            } else {
                if(!settingsUpdated) {
                    createLocalDB(weatherData)
                }
            }
        }
    }

    private fun createLocalDB(weatherData: WeatherData) {
        val currentSettings = sharedPreferencesHelper.getSettings()
        if (currentSettings != null) {
            val weatherHelper = WeatherHelper(currentSettings, weatherData)
            val newWeatherData = weatherHelper.convertWeatherData()

            if(newWeatherData != weatherData) {
                settingsUpdated = true
                weatherViewModel.updateWeatherData(newWeatherData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.viewModelScope.cancel("ActivityDestroying")
    }
}