package me.tangobee.weathernaut.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.constants.WeatherCodes
import me.tangobee.weathernaut.constants.WeatherImageMapper
import me.tangobee.weathernaut.databinding.FragmentHomeBinding
import me.tangobee.weathernaut.models.HourlyWeatherRVModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData
import me.tangobee.weathernaut.ui.adapter.HourlyWeatherRVAdapter
import me.tangobee.weathernaut.viewmodels.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var todayHourlyWeatherRVAdapter: HourlyWeatherRVAdapter
    private lateinit var tomorrowHourlyWeatherRVAdapter: HourlyWeatherRVAdapter
    private val todayHourlyWeatherRVModelList: ArrayList<HourlyWeatherRVModel> = ArrayList()
    private val tomorrowHourlyWeatherRVModelList: ArrayList<HourlyWeatherRVModel> = ArrayList()

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

        binding.next7DaysIndicator.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_next7DaysFragment)
        }

        binding.settings.setOnClickListener {
            // TODO("Create a screen for app settings");
        }

        binding.searchCities.setOnClickListener {
            // TODO("Create a screen for searching cities");
        }

        binding.todayIndicator.setOnClickListener {
            changeIndicatorDotPosition(R.id.todayIndicator)
            binding.hourlyWeatherRV.swapAdapter(todayHourlyWeatherRVAdapter, false)

            binding.todayIndicator.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            binding.tomorrowIndicator.setTextColor(ContextCompat.getColor(requireContext(), R.color.unselectedIndicatorColor))

            val boldTypeface = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.inter)
            binding.todayIndicator.typeface = boldTypeface
            binding.tomorrowIndicator.typeface = typeface

            binding.todayIndicator.isEnabled = false
            binding.tomorrowIndicator.isEnabled = true
        }

        binding.tomorrowIndicator.setOnClickListener {
            changeIndicatorDotPosition(R.id.tomorrowIndicator)
            binding.hourlyWeatherRV.swapAdapter(tomorrowHourlyWeatherRVAdapter, false)

            binding.tomorrowIndicator.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            binding.todayIndicator.setTextColor(ContextCompat.getColor(requireContext(), R.color.unselectedIndicatorColor))

            val boldTypeface = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.inter)
            binding.tomorrowIndicator.typeface = boldTypeface
            binding.todayIndicator.typeface = typeface

            binding.tomorrowIndicator.isEnabled = false
            binding.todayIndicator.isEnabled = true
        }


        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) {weatherData ->
            if(weatherData != null) {
                setCurrentWeatherUIData(weatherData)
                setHourlyRecyclerViewUIData(weatherData)
            }
        }
    }

    private fun setCurrentWeatherUIData(weatherData: WeatherData) {
        val city = "${weatherData.city}\n"
        val country = weatherData.country
        val region = SpannableString("$city$country")
        region.setSpan(
            RelativeSizeSpan(0.7f),
            city.length,
            region.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.locationName.text = region

        binding.date.text = convertISO8601ToCustomDateFormat(weatherData.current_weather.current.time)
        binding.currentWeatherTemperature.text = weatherData.current_weather.current.temperature_2m.toInt().toString()
        binding.weatherUnit.text = weatherData.current_weather.current_units.temperature_2m
        binding.weatherIcon.setImageResource(WeatherImageMapper.getImageForWeatherCode(weatherData.current_weather.current.weather_code))
        binding.currentWeatherType.text = WeatherCodes.weatherConstants.first {it.code == weatherData.current_weather.current.weather_code}.description

        val currentPressure = weatherData.current_weather.current.pressure_msl.toString() + weatherData.current_weather.current_units.pressure_msl
        binding.pressureValue.text = currentPressure

        val windPressure = weatherData.current_weather.current.wind_speed_10m.toString() + weatherData.current_weather.current_units.wind_speed_10m
        binding.windValue.text = windPressure

        val humidityPressure = weatherData.current_weather.current.relative_humidity_2m.toString() + weatherData.current_weather.current_units.relative_humidity_2m
        binding.humidityValue.text = humidityPressure
    }
    private fun setHourlyRecyclerViewUIData(weatherData: WeatherData) {
        var currentHourlyWeatherItemPosition = 0
        for(i in 0 .. 23) {
            var time = convertDateStringToFormattedTime(weatherData.hourly_weather.hourly.time[i])
            if(isCurrentLocalTime(time)) {
                time = "now"
                currentHourlyWeatherItemPosition = i
            }

            val weatherIcon = WeatherImageMapper.getImageForWeatherCode(weatherData.hourly_weather.hourly.weather_code[i])
            val weatherTemp = weatherData.hourly_weather.hourly.temperature_2m[i]

            todayHourlyWeatherRVModelList.add(HourlyWeatherRVModel(time, weatherIcon, "${weatherTemp.toInt()}°"))
        }

        for(i in 24 .. 47) {
            val time = convertDateStringToFormattedTime(weatherData.hourly_weather.hourly.time[i])
            val weatherIcon = WeatherImageMapper.getImageForWeatherCode(weatherData.hourly_weather.hourly.weather_code[i])
            val weatherTemp = weatherData.hourly_weather.hourly.temperature_2m[i]

            tomorrowHourlyWeatherRVModelList.add(HourlyWeatherRVModel(time, weatherIcon, "${weatherTemp.toInt()}°"))
        }

        binding.hourlyWeatherRV.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        todayHourlyWeatherRVAdapter = HourlyWeatherRVAdapter(todayHourlyWeatherRVModelList)
        tomorrowHourlyWeatherRVAdapter = HourlyWeatherRVAdapter(tomorrowHourlyWeatherRVModelList)
        binding.hourlyWeatherRV.adapter = todayHourlyWeatherRVAdapter

        binding.hourlyWeatherRV.scrollToPosition(currentHourlyWeatherItemPosition)

        binding.tomorrowIndicator.isEnabled = true
        binding.todayIndicator.isEnabled = true
    }

    private fun convertISO8601ToCustomDateFormat(iso8601Date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(iso8601Date)
        return date?.let { outputFormat.format(it) }
    }
    private fun convertDateStringToFormattedTime(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getDefault()

        val date = inputFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        return outputFormat.format(calendar.time)
    }

    private fun changeIndicatorDotPosition(viewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.homeFragmentLayout)
        constraintSet.connect(R.id.indicator, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        constraintSet.connect(R.id.indicator, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        constraintSet.applyTo(binding.homeFragmentLayout)
    }

    private fun isCurrentLocalTime(timeString: String): Boolean {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = Calendar.getInstance()
        val parsedTime = sdf.parse(timeString)

        if (parsedTime != null) {
            val parsedCalendar = Calendar.getInstance()
            parsedCalendar.time = parsedTime

            return currentTime.get(Calendar.HOUR_OF_DAY) == parsedCalendar.get(Calendar.HOUR_OF_DAY)
        }

        return false
    }
}