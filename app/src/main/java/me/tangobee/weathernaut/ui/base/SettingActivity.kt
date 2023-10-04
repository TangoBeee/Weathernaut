package me.tangobee.weathernaut.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        binding.temperatureUnit.setOnClickListener {showTemperatureUnitPopup(binding.temperatureSpinner)}
        binding.windSpeedUnit.setOnClickListener {showWindSpeedUnitPopup(binding.windSpeedSpinner)}
        binding.rainfallUnit.setOnClickListener {showRainfallUnitPopup(binding.rainFallSpinner)}

        binding.feedback.setOnClickListener { openURL(Uri.parse("mailto:feedback.weathernaut@gmail.com?subject=Feedback on Weathernaut app.")) }
        binding.termsAndConditions.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/terms-and-conditions")) }
        binding.privacyPolicy.setOnClickListener { openURL(Uri.parse("https://tangobee.netlify.app/Weathernaut/privacy-policy")) }
        binding.soundEffect.setOnClickListener { binding.soundEffectSwitch.setChecked(!binding.soundEffectSwitch.isChecked) }
        binding.back.setOnClickListener {onBackPressedDispatcher.onBackPressed()}
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
                R.id.beaufort -> {
                    binding.windSpeedSpinner.text = item.title
                }

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
    private fun showRainfallUnitPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.rainfall_unit_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.centimeters -> {
                    binding.rainFallSpinner.text = item.title
                }

                R.id.millimeters -> {
                    binding.rainFallSpinner.text = item.title
                }

                R.id.inches -> {
                    binding.rainFallSpinner.text = item.title
                }
            }

            true
        }

        popup.show()
    }

    private fun openURL(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

}