package me.tangobee.weathernaut

import android.os.Bundle
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
import me.tangobee.weathernaut.viewmodels.WeatherViewModel
import me.tangobee.weathernaut.viewmodels.WeatherViewModelFactory
import java.net.UnknownHostException
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)

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

        fetchData()
        splashScreen.setKeepOnScreenCondition { (weatherViewModel.weatherLiveData.value == null) }

        setContentView(binding.root)
    }

    private fun fetchData() {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(weatherRepository))[WeatherViewModel::class.java]

        if(weatherViewModel.weatherLiveData.value == null) {
            weatherViewModel.getWeather(coroutineExceptionHandler)
        }

        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            if(weatherData == null) {
                Toast.makeText(this, getString(R.string.api_fetching_error), Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
                exitProcess(0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.viewModelScope.cancel("ActivityDestroying")
    }
}