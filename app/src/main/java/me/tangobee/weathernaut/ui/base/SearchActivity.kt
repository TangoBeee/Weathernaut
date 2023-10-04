package me.tangobee.weathernaut.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        onBackPressedDispatcher.addCallback(this) {
            finish()
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

        binding.cancelSearch.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }
    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }
}