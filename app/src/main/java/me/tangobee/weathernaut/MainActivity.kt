package me.tangobee.weathernaut

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
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

    // For requesting notification permissions
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val noInternetLiveData : MutableLiveData<Boolean> = MutableLiveData(false)

        // Handle internet-related errors
        coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable is UnknownHostException) {
                noInternetLiveData.postValue(true)
            }
        }



        // Request notification permission for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        val settingsModel = SharedPreferencesHelper(this).getSettings()
        if (settingsModel?.isMusicOn != false) {
            val startMusicIntent = Intent(this, WeatherMusicService::class.java)
            startService(startMusicIntent)
        }

        fetchData()
        splashScreen.setKeepOnScreenCondition { (weatherViewModel.weatherLiveData.value == null) }

        setContentView(binding.root)
    }

    private fun requestNotificationPermission() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request notification permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun fetchData() {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(weatherRepository))[WeatherViewModel::class.java]

        val geocodingData = sharedPreferencesHelper.getGeocodingData()

        if (weatherViewModel.weatherLiveData.value == null) {
            if (geocodingData == null) {
                weatherViewModel.getWeather(coroutineExceptionHandler)
            } else {
                weatherViewModel.getGeoWeather(coroutineExceptionHandler, geocodingData)
            }
        }

        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            if (weatherData == null) {
                Toast.makeText(this, getString(R.string.api_fetching_error), Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
                exitProcess(0)
            } else {
                if (!settingsUpdated) {
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

            if (newWeatherData != weatherData) {
                settingsUpdated = true
                weatherViewModel.updateWeatherData(newWeatherData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.weatherLiveData.removeObservers(this)
        weatherViewModel.viewModelScope.cancel("ActivityDestroying")
    }
}
