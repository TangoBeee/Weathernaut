package me.tangobee.weathernaut.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.feedback.setOnClickListener { openURL(Uri.parse("mailto:hello.weathernaut@outlook.com?subject=Feedback on Weathernaut app.")) }
        binding.privacyPolicy.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/privacy-policy")) }
        binding.donation.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/donation")) }
        binding.credits.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/credits")) }

        binding.temperatureUnit.setOnClickListener {showTemperatureUnitPopup(binding.temperatureSpinner)}
        binding.windSpeedUnit.setOnClickListener {showWindSpeedUnitPopup(binding.windSpeedSpinner)}
        binding.atmosphericPressureUnit.setOnClickListener {showAtmosphericPressureUnitPopup(binding.atmosphericPressureSpinner)}

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.weatherMusicSwitch.setOnClickListener {
            val flag = binding.weatherMusicSwitch.isChecked
            binding.weatherMusicSwitch.setChecked(!flag)

            weatherMusicToggle(!flag)
        }

        binding.weatherMusicWrapper.setOnClickListener {
            val flag = binding.weatherMusicSwitch.isChecked
            binding.weatherMusicSwitch.setChecked(!flag)

            weatherMusicToggle(!flag)
        }
    }

    private fun weatherMusicToggle(musicFlag: Boolean) {
        Toast.makeText(this, "Music is turned " + (if(musicFlag) "on" else "off"), Toast.LENGTH_SHORT).show()
    }

    private fun openURL(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun showTemperatureUnitPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.temperature_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.celsius -> {
                    binding.temperatureSpinner.text = item.title
                }

                R.id.fahrenheit -> {
                    binding.temperatureSpinner.text = item.title
                }
            }

            true
        }

        popup.show()
    }
    private fun showWindSpeedUnitPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.windsped_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.kilometers -> {
                    binding.windSpeedSpinner.text = item.title
                }

                R.id.meters -> {
                    binding.windSpeedSpinner.text = item.title
                }

                R.id.miles -> {
                    binding.windSpeedSpinner.text = item.title
                }
            }

            true
        }

        popup.show()
    }
    private fun showAtmosphericPressureUnitPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.atmospheric_pressure_unit_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.hpa -> {
                    binding.atmosphericPressureSpinner.text = item.title
                }

                R.id.atm -> {
                    binding.atmosphericPressureSpinner.text = item.title
                }
            }

            true
        }

        popup.show()
    }
}