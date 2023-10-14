package me.tangobee.weathernaut.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.adapter.HorizontalWeatherAdapter
import me.tangobee.weathernaut.data.RetrofitHelper
import me.tangobee.weathernaut.data.local.LocationSharedPrefService
import me.tangobee.weathernaut.data.local.SettingsSharedPrefService
import me.tangobee.weathernaut.data.local.WeatherSharedPrefService
import me.tangobee.weathernaut.data.remote.CurrentLocationService
import me.tangobee.weathernaut.data.remote.WeatherService
import me.tangobee.weathernaut.data.repository.CurrentLocationRepository
import me.tangobee.weathernaut.data.repository.LocationSharedPrefRepository
import me.tangobee.weathernaut.data.repository.SettingsSharedPrefRepository
import me.tangobee.weathernaut.data.repository.WeatherRepository
import me.tangobee.weathernaut.data.repository.WeatherSharedPrefRepository
import me.tangobee.weathernaut.databinding.FragmentHomeBinding
import me.tangobee.weathernaut.model.CurrentLocationData
import me.tangobee.weathernaut.model.SettingsData
import me.tangobee.weathernaut.model.WeatherTimeCardData
import me.tangobee.weathernaut.model.weathermodel.WeatherData
import me.tangobee.weathernaut.ui.base.SearchActivity
import me.tangobee.weathernaut.ui.base.SettingActivity
import me.tangobee.weathernaut.util.AppConstants
import me.tangobee.weathernaut.util.CountryNameByCode
import me.tangobee.weathernaut.util.InternetConnection
import me.tangobee.weathernaut.util.NavigateFragmentUtil
import me.tangobee.weathernaut.viewmodel.CurrentLocationViewModel
import me.tangobee.weathernaut.viewmodel.LocationSharedPrefViewModel
import me.tangobee.weathernaut.viewmodel.SettingsSharedPrefViewModel
import me.tangobee.weathernaut.viewmodel.WeatherSharedPrefViewModel
import me.tangobee.weathernaut.viewmodel.WeatherViewModel
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.CurrentLocationViewModelFactory
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.LocationSharedPrefViewModelFactory
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.SettingsSharedPrefViewModelFactory
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.WeatherSharedPrefViewModelFactory
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.WeatherViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var weatherCardData: ArrayList<WeatherTimeCardData>

    private lateinit var weatherViewModel: WeatherViewModel

    private lateinit var settingsData: SettingsData

    private lateinit var currentLocationViewModel: CurrentLocationViewModel
    private lateinit var locationSharedPrefViewModel: LocationSharedPrefViewModel
    private lateinit var settingSharedPrefViewModel: SettingsSharedPrefViewModel
    private lateinit var weatherSharedPrefViewModel: WeatherSharedPrefViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setting data in UI
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
        binding.date.text = dateFormat.format(currentDate)

        //First get location then set the location data in local and in UI after that get weather data from API
        initCurrentLocationThing()
        initLocationSharedPrefThing()
        initWeatherSharedPrefThing()
        initSettingsSharedPrefThing()
        initWeatherAPIThing()

        //Getting and setting data from LocationSharedPrefViewModel to UI
        val locationSharedPrefData = locationSharedPrefViewModel.getData()
        locationSharedPrefData?.let { setLocationDataToUI(locationSharedPrefData) }

        //Getting Data from Settings SharedPref
        settingsData = settingSharedPrefViewModel.getData() ?: SettingsData()
        setSettingDataToUI(settingsData)

        //Getting and setting data from WeatherSharedPref to UI
        val weatherData = weatherSharedPrefViewModel.getData()
        weatherData?.let { setWeatherDataToUI(it) }

        //Observing LiveData from CurrentLocationViewModel
        currentLocationViewModel.approximateLocationLiveData.observe(viewLifecycleOwner) {
            if(it != null) {
                if(locationSharedPrefData == null || locationSharedPrefData.loc != it.loc) {
                    locationSharedPrefViewModel.sendData(it)
                    setLocationDataToUI(it)
                }
            }
        }

        //Calling Weather API
        val loc = locationSharedPrefData?.loc?.split(",")
        if(loc != null) {
            val lat = loc[0]
            val lon = loc[1]
            if(InternetConnection.isNetworkAvailable(requireContext())) {
                lifecycleScope.launch {
                    weatherViewModel.getWeather(lat, lon, resources.getString(R.string.api_key))
                }
            } else {
                val snackBar = Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(R.string.try_again) {
                    if(InternetConnection.isNetworkAvailable(requireContext())) {
                        lifecycleScope.launch {
                            weatherViewModel.getWeather(lat, lon, resources.getString(R.string.api_key))
                        }
                    } else {
                        Toast.makeText(requireContext(), "No internet connection. Please try later.", Toast.LENGTH_SHORT).show()
                    }
                }.show()
            }
        }
        //Observing the livedata from WeatherAPI
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) {
            if(it != null) {
                setWeatherDataToUI(it)
                weatherSharedPrefViewModel.sendData(it)
            }
        }

        addSampleData()
        setHorizontalWeatherViewAdapter()

        binding.settings.setOnClickListener { moveToSettings() }
        binding.search.setOnClickListener {moveToSearch()}
        binding.next7days.setOnClickListener { navToUpcomingDaysFrag() }
        binding.today.setOnClickListener { changeWeatherToToday() }
        binding.tomorrow.setOnClickListener { changeWeatherToTomorrow() }
    }

    private fun setLocationDataToUI(currentLocation: CurrentLocationData) {
        binding.cityName.text = currentLocation.city
        binding.countryName.text = CountryNameByCode.getCountryNameByCode(requireContext(), currentLocation.country)
    }

    private fun setWeatherDataToUI(weatherData: WeatherData) {
        var temp = weatherData.main.temp

        if(settingsData.temperatureUnit == resources.getString(R.string.fahrenheit)) {
            temp = (temp-273.15)*1.8+32
        } else {
            temp -= 273.15
        }

        if(settingsData.atmosphericPressureUnit == resources.getString(R.string.atm)) {
            val pressure = weatherData.main.pressure * 0.000987
            binding.atmosphericPressureValue.text = String.format("%.6f", pressure)
        } else {
            binding.atmosphericPressureValue.text = String.format("%.6f", weatherData.main.pressure)
        }

        if(settingsData.windSpeedUnit == resources.getString(R.string.miles_per_hour)) {
            val speed = weatherData.wind.speed*2.237
            binding.windValue.text = String.format("%.2f", speed)
        } else if(settingsData.windSpeedUnit == resources.getString(R.string.kilometers_per_hour)) {
            val speed = weatherData.wind.speed*3.6
            binding.windValue.text = String.format("%.2f", speed)
        } else {
            binding.windValue.text = weatherData.wind.speed.toString()
        }

        binding.weatherNumericValue.text = String.format("%.0f", temp)
        binding.humidityValue.text = weatherData.main.humidity.toString()
    }

    private fun setSettingDataToUI(settingsData: SettingsData) {
        if(settingsData.temperatureUnit == resources.getString(R.string.celsius)) {
            binding.weatherUnit.text = resources.getString(R.string.celsius)
        } else {
            binding.weatherUnit.text = resources.getString(R.string.fahrenheit)
        }

        binding.atmosphericPressureUnit.text = settingsData.atmosphericPressureUnit
        binding.windUnit.text = settingsData.windSpeedUnit
    }

    private fun changeWeatherToToday() {
        val todayTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        val tomorrowTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter)

        binding.today.typeface = todayTypeface
        binding.tomorrow.typeface = tomorrowTypeface

        binding.today.setTextColor(requireActivity().getColor(R.color.textColor))
        binding.tomorrow.setTextColor(Color.parseColor("#D6996B"))

        changeIndicatorDotPosition(R.id.today)

        //Adding Today's Weather Data in array and adapter
        addSampleData()
        setHorizontalWeatherViewAdapter()
    }

    private fun changeWeatherToTomorrow() {
        val tomorrowTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        val todayTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter)

        binding.tomorrow.typeface = tomorrowTypeface
        binding.today.typeface = todayTypeface

        binding.tomorrow.setTextColor(requireActivity().getColor(R.color.textColor))
        binding.today.setTextColor(Color.parseColor("#D6996B"))

        changeIndicatorDotPosition(R.id.tomorrow)

        //Adding Tomorrow's Weather Data in array and adapter
        addSampleData1()
        setHorizontalWeatherViewAdapter()
    }

    private fun changeIndicatorDotPosition(viewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.parentLayout)
        constraintSet.connect(R.id.indicator, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        constraintSet.connect(R.id.indicator, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        constraintSet.applyTo(binding.parentLayout)
    }

    private fun setHorizontalWeatherViewAdapter() {
        binding.smallWeatherCardView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val adapter = HorizontalWeatherAdapter(weatherCardData)
        binding.smallWeatherCardView.adapter = adapter
    }

    private fun moveToSettings() {
        val intent = Intent(requireContext(), SettingActivity::class.java)
        startActivity(intent)
    }

    private fun moveToSearch() {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        startActivity(intent)
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        } else {
            requireActivity().overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.fadein, R.anim.fadeout)
        }
    }

    private fun navToUpcomingDaysFrag() {
        val navHelper = NavigateFragmentUtil()
        navHelper.navigateToFragment(requireView(), R.id.nav_homeFrag_to_upcomingDaysFrag)
    }

    private fun initWeatherAPIThing() {
        //Initialization of GeoLocationRepository and GeoLocationServices
        val weatherService = RetrofitHelper.getInstance(AppConstants.OpenWeatherMap_API_BASE_URL).create(
            WeatherService::class.java)
        val weatherRepository = WeatherRepository(weatherService)

        //Initialization of GeoLocationViewModel
        weatherViewModel = ViewModelProvider(this@HomeFragment, WeatherViewModelFactory(weatherRepository))[WeatherViewModel::class.java]
    }

    private fun initCurrentLocationThing() {
        //Initialization of CurrentLocationRepository and CurrentLocationServices
        val currentLocationService = RetrofitHelper.getInstance(AppConstants.CURRENT_LOCATION_API_BASE_URL).create(CurrentLocationService::class.java)
        val currentLocationRepository = CurrentLocationRepository(currentLocationService)

        //Initialization of CurrentLocationViewModel
        currentLocationViewModel = ViewModelProvider(this@HomeFragment, CurrentLocationViewModelFactory(currentLocationRepository))[CurrentLocationViewModel::class.java]

        //Calling API to get current location
        if(InternetConnection.isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                currentLocationViewModel.getLocation()
            }
        } else {
            val snackBar =
                Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(R.string.try_again) {
                if (InternetConnection.isNetworkAvailable(requireContext())) {
                    lifecycleScope.launch {
                        currentLocationViewModel.getLocation()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No internet connection. Please try later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.show()
        }
    }

    private fun initLocationSharedPrefThing() {
        val locationSharedPrefService = LocationSharedPrefService(requireContext())
        val locationSharedPrefRepository = LocationSharedPrefRepository(locationSharedPrefService)

        locationSharedPrefViewModel = ViewModelProvider(this@HomeFragment, LocationSharedPrefViewModelFactory(locationSharedPrefRepository))[LocationSharedPrefViewModel::class.java]
    }

    private fun initSettingsSharedPrefThing() {
        val settingsSharedPrefService = SettingsSharedPrefService(requireContext())
        val settingsSharedPrefRepository = SettingsSharedPrefRepository(settingsSharedPrefService)

        settingSharedPrefViewModel = ViewModelProvider(this@HomeFragment, SettingsSharedPrefViewModelFactory(settingsSharedPrefRepository))[SettingsSharedPrefViewModel::class.java]
    }

    private fun initWeatherSharedPrefThing() {
        val weatherSharedPrefService = WeatherSharedPrefService(requireContext())
        val weatherSharedPrefRepository = WeatherSharedPrefRepository(weatherSharedPrefService)

        weatherSharedPrefViewModel = ViewModelProvider(this@HomeFragment, WeatherSharedPrefViewModelFactory(weatherSharedPrefRepository))[WeatherSharedPrefViewModel::class.java]
    }

    private fun addSampleData() {
        weatherCardData = ArrayList()
        weatherCardData.add(WeatherTimeCardData("11:00", R.drawable.icon_weather_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("12:00", R.drawable.icon_weather_cloud_sun, "19°", false))
        weatherCardData.add(WeatherTimeCardData("13:00", R.drawable.icon_weather_sun, "21°", false))
        weatherCardData.add(WeatherTimeCardData("14:00", R.drawable.icon_weather_rain_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("15:00", R.drawable.icon_weather_sun_rain_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("16:00", R.drawable.icon_weather_cloud, "19°", true))
        weatherCardData.add(WeatherTimeCardData("17:00", R.drawable.icon_weather_rain_cloud, "17°", false))
        weatherCardData.add(WeatherTimeCardData("18:00", R.drawable.icon_weather_cloud_sun, "20°", false))
    }


    private fun addSampleData1() {
        weatherCardData = ArrayList()
        weatherCardData.add(WeatherTimeCardData("02:00", R.drawable.icon_weather_rain_cloud, "16°", false))
        weatherCardData.add(WeatherTimeCardData("03:00", R.drawable.icon_weather_rain_cloud, "14°", false))
        weatherCardData.add(WeatherTimeCardData("04:00", R.drawable.icon_weather_cloud_sun, "20°", false))
        weatherCardData.add(WeatherTimeCardData("05:00", R.drawable.icon_weather_cloud, "19°", false))
        weatherCardData.add(WeatherTimeCardData("06:00", R.drawable.icon_weather_sun, "21°", false))
        weatherCardData.add(WeatherTimeCardData("07:00", R.drawable.icon_weather_cloud, "18°", false))
        weatherCardData.add(WeatherTimeCardData("08:00", R.drawable.icon_weather_rain_cloud, "17°", false))
        weatherCardData.add(WeatherTimeCardData("09:00", R.drawable.icon_weather_sun_rain_cloud, "20°", false))
    }

}