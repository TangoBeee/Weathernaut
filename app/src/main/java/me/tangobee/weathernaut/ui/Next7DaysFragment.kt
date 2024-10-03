package me.tangobee.weathernaut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.constants.WeatherImageMapper
import me.tangobee.weathernaut.databinding.FragmentNext7DaysBinding
import me.tangobee.weathernaut.models.DailyWeatherRVModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData
import me.tangobee.weathernaut.ui.adapter.DailyWeatherRVAdapter
import me.tangobee.weathernaut.viewmodels.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class Next7DaysFragment : Fragment() {

    private lateinit var binding: FragmentNext7DaysBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var dailyWeatherRVModelList: ArrayList<DailyWeatherRVModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNext7DaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) {weatherData ->
            if(weatherData != null) {
                setDailyWeatherRecyclerViewUIData(weatherData)
            }
        }

        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

    private fun setDailyWeatherRecyclerViewUIData(weatherData: WeatherData) {
        val tomorrowWeatherTemp = (weatherData.daily_weather.daily.temperature_2m_min[1].toInt().toString() + "/" + weatherData.daily_weather.daily.temperature_2m_max[1].toInt().toString())
        binding.tomorrowWeatherTemperature.text = tomorrowWeatherTemp
        binding.sunriseTime.text = convertDateStringToFormattedTime(weatherData.daily_weather.daily.sunrise[1])
        binding.sunsetTime.text = convertDateStringToFormattedTime(weatherData.daily_weather.daily.sunset[1])
        binding.tomorrowWeatherIcon.setImageResource(WeatherImageMapper.getImageForWeatherCode(weatherData.daily_weather.daily.weather_code[1]))

        dailyWeatherRVModelList = ArrayList()
        for(i in 2..7) {
            val day = getDayOfWeek(weatherData.daily_weather.daily.time[i])
            val weatherTemp = (weatherData.daily_weather.daily.temperature_2m_min[i].toInt().toString() + "/" + weatherData.daily_weather.daily.temperature_2m_max[i].toInt().toString())
            val weatherIcon = WeatherImageMapper.getImageForWeatherCode(weatherData.daily_weather.daily.weather_code[i])
            dailyWeatherRVModelList.add(DailyWeatherRVModel(day, weatherTemp, weatherIcon))
        }

        val next7DaysWeatherRVLinearLayoutManager = object : LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.next7DaysWeatherRV.setHasFixedSize(true)
        binding.next7DaysWeatherRV.layoutManager = next7DaysWeatherRVLinearLayoutManager
        val next7DaysRVAdapter = DailyWeatherRVAdapter(dailyWeatherRVModelList)
        binding.next7DaysWeatherRV.adapter = next7DaysRVAdapter
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

    private fun getDayOfWeek(dateStr: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dateStr)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        val days = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val days_es = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val days_fr = arrayOf("Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi")
        val days_de = arrayOf("Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag")

        val lang = requireContext().resources.configuration.locale.language
        when(lang){
            "en" -> return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            "es" -> return days_es[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            "fr" -> return days_fr[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            "de" -> return days_de[calendar.get(Calendar.DAY_OF_WEEK) - 1]
        }

        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

}