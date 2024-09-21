package me.tangobee.weathernaut.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.constants.UnitsMapper
import me.tangobee.weathernaut.databinding.FragmentSettingsBinding
import me.tangobee.weathernaut.models.SettingsModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData
import me.tangobee.weathernaut.utils.SharedPreferencesHelper
import me.tangobee.weathernaut.utils.WeatherHelper
import me.tangobee.weathernaut.viewmodels.WeatherViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var settingsModel: SettingsModel

    private lateinit var weatherData: WeatherData

    private lateinit var mainContainerView: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainContainerView = requireActivity().findViewById(R.id.mainContainerView)
        mainContainerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherData ->
            if(weatherData != null) {
                this.weatherData = weatherData
            }
        }

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        settingsModel = sharedPreferencesHelper.getSettings() ?: SettingsModel()
        sharedPreferencesHelper.saveSettings(settingsModel)
        setSettingsUIDate()

        binding.feedback.setOnClickListener { openURL(Uri.parse("mailto:hello.weathernaut@outlook.com?subject=Feedback on Weathernaut app.")) }
        binding.privacyPolicy.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/privacy-policy")) }
        binding.donation.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/donation")) }
        binding.credits.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/credits")) }

        binding.temperatureUnit.setOnClickListener {showTemperatureUnitPopup(binding.temperatureSpinner)}
        binding.windSpeedUnit.setOnClickListener {showWindSpeedUnitPopup(binding.windSpeedSpinner)}
        binding.atmosphericPressureUnit.setOnClickListener {showAtmosphericPressureUnitPopup(binding.atmosphericPressureSpinner)}

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.weatherMusicSwitch.setOnClickListener {
            val flag = binding.weatherMusicSwitch.isChecked
            binding.weatherMusicSwitch.setChecked(!flag)


            sharedPreferencesHelper.updateSettings {
                it.isMusicOn = !flag
                it
            }

            val currentSettings = sharedPreferencesHelper.getSettings()
            updateWeatherData(currentSettings)

            weatherMusicToggle(!flag)
        }

        binding.weatherMusicWrapper.setOnClickListener {
            val flag = binding.weatherMusicSwitch.isChecked
            binding.weatherMusicSwitch.setChecked(!flag)

            sharedPreferencesHelper.updateSettings {
                it.isMusicOn = !flag
                it
            }

            val currentSettings = sharedPreferencesHelper.getSettings()
            updateWeatherData(currentSettings)

            weatherMusicToggle(!flag)
        }
    }

    private fun setSettingsUIDate() {
        binding.temperatureSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.tempUnit)
        binding.windSpeedSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.windSpeedUnit)
        binding.atmosphericPressureSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.pressureUnit)

        binding.weatherMusicSwitch.setChecked(settingsModel.isMusicOn)
    }

    private fun weatherMusicToggle(musicFlag: Boolean) {
        Toast.makeText(requireContext(), "Music is turned " + (if(musicFlag) "on" else "off"), Toast.LENGTH_SHORT).show()
    }

    private fun openURL(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun showTemperatureUnitPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.temperature_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.celsius -> {
                    binding.temperatureSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.tempUnit = UnitsMapper.getFullUnit(binding.temperatureSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }

                R.id.fahrenheit -> {
                    binding.temperatureSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.tempUnit = UnitsMapper.getFullUnit(binding.temperatureSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }
            }

            true
        }

        popup.show()
    }
    private fun showWindSpeedUnitPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.windsped_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.kilometers -> {
                    binding.windSpeedSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.windSpeedUnit = UnitsMapper.getFullUnit(binding.windSpeedSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }

                R.id.meters -> {
                    binding.windSpeedSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.windSpeedUnit = UnitsMapper.getFullUnit(binding.windSpeedSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }

                R.id.miles -> {
                    binding.windSpeedSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.windSpeedUnit = UnitsMapper.getFullUnit(binding.windSpeedSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }
            }

            true
        }

        popup.show()
    }
    private fun showAtmosphericPressureUnitPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.atmospheric_pressure_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.hpa -> {
                    binding.atmosphericPressureSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.pressureUnit = UnitsMapper.getFullUnit(binding.atmosphericPressureSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }

                R.id.atm -> {
                    binding.atmosphericPressureSpinner.text = item.title

                    sharedPreferencesHelper.updateSettings {
                        it.pressureUnit = UnitsMapper.getFullUnit(binding.atmosphericPressureSpinner.text.toString())
                        it
                    }

                    val currentSettings = sharedPreferencesHelper.getSettings()
                    updateWeatherData(currentSettings)
                }
            }

            true
        }

        popup.show()
    }

    private fun updateWeatherData(currentSettings: SettingsModel?) {
        if(currentSettings != null) {
            val weatherHelper = WeatherHelper(currentSettings, weatherData)
            val newWeatherData = weatherHelper.convertWeatherData()

            weatherViewModel.updateWeatherData(newWeatherData)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainContainerView.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_background)
    }
}