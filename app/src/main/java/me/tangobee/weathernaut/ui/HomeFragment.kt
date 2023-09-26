package me.tangobee.weathernaut.ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.adapter.HorizontalWeatherAdapter
import me.tangobee.weathernaut.databinding.FragmentHomeBinding
import me.tangobee.weathernaut.model.WeatherTimeCardData
import me.tangobee.weathernaut.util.NavigateFragmentUtil

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var weatherCardData: ArrayList<WeatherTimeCardData>

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

        setHorizontalWeatherViewAdapter()

        binding.settings.setOnClickListener { moveToSettings() }
        binding.search.setOnClickListener {moveToSearch()}
        binding.next7days.setOnClickListener { navToUpcomingDaysFrag() }
        binding.today.setOnClickListener { changeWeatherToToday() }
        binding.tomorrow.setOnClickListener { changeWeatherToTomorrow() }
    }

    private fun changeWeatherToToday() {
        val todayTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        val tomorrowTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter)

        binding.today.typeface = todayTypeface
        binding.tomorrow.typeface = tomorrowTypeface

        binding.today.setTextColor(requireActivity().getColor(R.color.textColor))
        binding.tomorrow.setTextColor(Color.parseColor("#D6996B"))

        changeIndicatorDotPosition(R.id.today)
    }

    private fun changeWeatherToTomorrow() {
        val tomorrowTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        val todayTypeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.inter)

        binding.tomorrow.typeface = tomorrowTypeface
        binding.today.typeface = todayTypeface

        binding.tomorrow.setTextColor(requireActivity().getColor(R.color.textColor))
        binding.today.setTextColor(Color.parseColor("#D6996B"))

        changeIndicatorDotPosition(R.id.tomorrow)
    }

    private fun changeIndicatorDotPosition(viewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.parentLayout)
        constraintSet.connect(R.id.indicator, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        constraintSet.connect(R.id.indicator, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        constraintSet.applyTo(binding.parentLayout)
    }

    private fun setHorizontalWeatherViewAdapter() {
        binding.smallWeatherCardView.setHasFixedSize(true)
        binding.smallWeatherCardView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        weatherCardData = ArrayList()

        addSampleData()

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

    private fun addSampleData() {
        weatherCardData.add(WeatherTimeCardData("11:00", R.drawable.icon_weather_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("12:00", R.drawable.icon_weather_cloud_sun, "19°", false))
        weatherCardData.add(WeatherTimeCardData("13:00", R.drawable.icon_weather_sun, "21°", false))
        weatherCardData.add(WeatherTimeCardData("14:00", R.drawable.icon_weather_rain_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("15:00", R.drawable.icon_weather_sun_rain_cloud, "20°", false))
        weatherCardData.add(WeatherTimeCardData("16:00", R.drawable.icon_weather_cloud, "19°", true))
        weatherCardData.add(WeatherTimeCardData("17:00", R.drawable.icon_weather_rain_cloud, "17°", false))
        weatherCardData.add(WeatherTimeCardData("18:00", R.drawable.icon_weather_cloud_sun, "20°", false))
    }

}