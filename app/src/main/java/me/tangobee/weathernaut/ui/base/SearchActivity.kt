package me.tangobee.weathernaut.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.adapter.SearchCitiesAdapter
import me.tangobee.weathernaut.databinding.ActivitySearchBinding
import me.tangobee.weathernaut.model.LocationData

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var citiesList: ArrayList<LocationData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        addSampleData()
        setSearchCitiesAdapter()

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

    private fun setSearchCitiesAdapter() {
        binding.citiesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val adapter = SearchCitiesAdapter(citiesList)
        binding.citiesRecyclerView.adapter = adapter
    }

    private fun addSampleData() {
        citiesList = ArrayList()

        citiesList.add(LocationData("Mathura", "IN" , "Uttar Pradesh", true))
        citiesList.add(LocationData("Mathura", "IN","Odisha", false))
        citiesList.add(LocationData("Mathura", "IN", "West Bengal", false))
        citiesList.add(LocationData("Mathur", "IN", "Tamil Nadu", false))
        citiesList.add(LocationData("Mathur", "IN", "Karnataka", false))
        citiesList.add(LocationData("Mathura Sultanpur Pachkatiya", "IN", "Bihar", false))
    }
}