package me.tangobee.weathernaut.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.constants.UnitsMapper
import me.tangobee.weathernaut.databinding.FragmentSettingsBinding
import me.tangobee.weathernaut.models.SettingsModel
import me.tangobee.weathernaut.models.WeatherData.WeatherData
import me.tangobee.weathernaut.services.WeatherMusicService
import me.tangobee.weathernaut.utils.LanguageHelper.changeLanguage
import me.tangobee.weathernaut.utils.LanguageHelper.setLocale
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

    @SuppressLint("SetTextI18n")
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
        binding.language.setOnClickListener {showLanguagePopup(binding.languageSpinner)}
        binding.language.apply {
            val lang = context.resources.configuration.locale.language
            setUpLangSpinner(lang)
        }

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

    private fun setUpLangSpinner(lang: String) {
        when(lang){
            "en" -> {
                binding.languageSpinner.text = resources.getString(R.string.english)
                binding.flagIcon.setImageResource(R.drawable.country_flag_uk)
            }
            "es" -> {
                binding.languageSpinner.text = resources.getString(R.string.spanish)
                binding.flagIcon.setImageResource(R.drawable.country_flag_es)
            }
            "fr" -> {
                binding.languageSpinner.text = resources.getString(R.string.french)
                binding.flagIcon.setImageResource(R.drawable.country_flag_fr)
            }
            "de" -> {
                binding.languageSpinner.text = resources.getString(R.string.german)
                binding.flagIcon.setImageResource(R.drawable.country_flag_de)
            }
        }
    }

    private fun setSettingsUIDate() {
        binding.temperatureSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.tempUnit)
        binding.windSpeedSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.windSpeedUnit)
        binding.atmosphericPressureSpinner.text = UnitsMapper.getShorthandUnit(settingsModel.pressureUnit)

        binding.weatherMusicSwitch.setChecked(settingsModel.isMusicOn)
    }

    private fun weatherMusicToggle(musicFlag: Boolean) {
        if(musicFlag) {
            Toast.makeText(requireContext(), "The music will start in a few seconds!", Toast.LENGTH_SHORT).show()
            val startMusicIntent = Intent(requireContext(), WeatherMusicService::class.java)
            requireActivity().startService(startMusicIntent)
        }
        else {
            val stopMusicIntent = Intent(requireContext(), WeatherMusicService::class.java)
            requireActivity().stopService(stopMusicIntent)
        }
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
    private fun showLanguagePopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.language_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.english -> {
                    setLocale(requireContext(),"en", resources)
                    changeLanguage(sharedPreferencesHelper, "en")
                    binding.languageSpinner.text = item.title
                    binding.flagIcon.setImageResource(R.drawable.country_flag_uk)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.spanish -> {
                    setLocale(requireContext(),"es", resources)
                    changeLanguage(sharedPreferencesHelper, "es")
                    binding.languageSpinner.text = item.title
                    binding.flagIcon.setImageResource(R.drawable.country_flag_es)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.french -> {
                    setLocale(requireContext(),"fr", resources)
                    changeLanguage(sharedPreferencesHelper, "fr")
                    binding.languageSpinner.text = item.title
                    binding.flagIcon.setImageResource(R.drawable.country_flag_fr)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.german -> {
                    setLocale(requireContext(),"de", resources)
                    changeLanguage(sharedPreferencesHelper, "de")
                    binding.languageSpinner.text = item.title
                    binding.flagIcon.setImageResource(R.drawable.country_flag_de)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
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

