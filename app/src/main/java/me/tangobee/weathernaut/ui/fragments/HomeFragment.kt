package me.tangobee.weathernaut.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.adapter.HorizontalWeatherAdapter
import me.tangobee.weathernaut.data.RetrofitHelper
import me.tangobee.weathernaut.data.local.LocationSharedPrefService
import me.tangobee.weathernaut.data.remote.CurrentLocationService
import me.tangobee.weathernaut.data.repository.CurrentLocationRepository
import me.tangobee.weathernaut.data.repository.LocationSharedPrefRepository
import me.tangobee.weathernaut.databinding.FragmentHomeBinding
import me.tangobee.weathernaut.model.CurrentLocationData
import me.tangobee.weathernaut.model.WeatherTimeCardData
import me.tangobee.weathernaut.ui.base.SearchActivity
import me.tangobee.weathernaut.ui.base.SettingActivity
import me.tangobee.weathernaut.util.AppConstants
import me.tangobee.weathernaut.util.CountryNameByCode
import me.tangobee.weathernaut.util.NavigateFragmentUtil
import me.tangobee.weathernaut.viewmodel.CurrentLocationViewModel
import me.tangobee.weathernaut.viewmodel.LocationSharedPrefViewModel
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.CurrentLocationViewModelFactory
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.LocationSharedPrefViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var weatherCardData: ArrayList<WeatherTimeCardData>

    private lateinit var currentLocationViewModel: CurrentLocationViewModel
    private lateinit var locationSharedPrefViewModel: LocationSharedPrefViewModel

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

        initCurrentLocationThing()
        initLocationSharedPrefThing()

        //Getting and setting data from LocationSharedPrefViewModel to UI
        val locationSharedPrefData = locationSharedPrefViewModel.getData()
        if(locationSharedPrefData != null) setDataToUI(locationSharedPrefData)

        //Observing LiveData from CurrentLocationViewModel
        currentLocationViewModel.approximateLocationLiveData.observe(viewLifecycleOwner) {
            if(it != null) {
                if(locationSharedPrefData == null || locationSharedPrefData.loc != it.loc) {
                    locationSharedPrefViewModel.sendData(it)
                    setDataToUI(it)
                }
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

    private fun setDataToUI(currentLocation: CurrentLocationData) {
        binding.cityName.text = currentLocation.city
        binding.countryName.text = CountryNameByCode.getCountryNameByCode(requireContext(), currentLocation.country)
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
        requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    private fun navToUpcomingDaysFrag() {
        val navHelper = NavigateFragmentUtil()
        navHelper.navigateToFragment(requireView(), R.id.nav_homeFrag_to_upcomingDaysFrag)
    }

    private fun initCurrentLocationThing() {
        //Initialization of CurrentLocationRepository and CurrentLocationServices
        val currentLocationService = RetrofitHelper.getInstance(AppConstants.CURRENT_LOCATION_API_BASE_URL).create(CurrentLocationService::class.java)
        val currentLocationRepository = CurrentLocationRepository(currentLocationService)

        //Initialization of CurrentLocationViewModel
        currentLocationViewModel = ViewModelProvider(this@HomeFragment, CurrentLocationViewModelFactory(currentLocationRepository))[CurrentLocationViewModel::class.java]

        //Calling API to get current location
        lifecycleScope.launch {
            currentLocationViewModel.getLocation()
        }
    }

    private fun initLocationSharedPrefThing() {
        val locationSharedPrefService = LocationSharedPrefService(requireContext())
        val locationSharedPrefRepository = LocationSharedPrefRepository(locationSharedPrefService)

        locationSharedPrefViewModel = ViewModelProvider(this@HomeFragment, LocationSharedPrefViewModelFactory(locationSharedPrefRepository))[LocationSharedPrefViewModel::class.java]
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