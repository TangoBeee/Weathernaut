package me.tangobee.weathernaut.ui.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.adapter.SearchCitiesAdapter
import me.tangobee.weathernaut.data.RetrofitHelper
import me.tangobee.weathernaut.data.remote.GeoLocationService
import me.tangobee.weathernaut.data.repository.GeoLocationRepository
import me.tangobee.weathernaut.databinding.ActivitySearchBinding
import me.tangobee.weathernaut.model.CityLocationData
import me.tangobee.weathernaut.util.AppConstants
import me.tangobee.weathernaut.viewmodel.GeoLocationViewModel
import me.tangobee.weathernaut.viewmodel.viewmodelfactory.GeoLocationViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var geoLocationViewModel: GeoLocationViewModel

    private lateinit var citiesList: CityLocationData

    private val CITY_LIMITS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Changing status bar color to black
        setStatusBarColor()

        //On Press Back Navigation Button
        onBackPressedDispatcher.addCallback(this) {
            finish()
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            } else {
                overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.fadein, R.anim.fadeout)
            }
        }

        //On Press Back ImageButton
        binding.cancelSearch.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            } else {
                overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.fadein, R.anim.fadeout)
            }
        }

        //Initialization of GeoLocationRepository and GeoLocationServices
        val geoLocationService = RetrofitHelper.getInstance(AppConstants.OpenWeatherMap_API_BASE_URL).create(GeoLocationService::class.java)
        val geoLocationRepository = GeoLocationRepository(geoLocationService)

        //Initialization of GeoLocationViewModel
        geoLocationViewModel = ViewModelProvider(this@SearchActivity, GeoLocationViewModelFactory(geoLocationRepository))[GeoLocationViewModel::class.java]

        //Observing LiveData from GeoLocationViewModel
        geoLocationViewModel.locationLiveData.observe(this@SearchActivity) {
            if(!it.isEmpty()) {
                citiesList = it

                for(city in citiesList) {
                    //TODO("Get the local city, state and country name and compare them with the cityItem that we get from the API")
                    if(city.name == "Delhi" && city.state == "Delhi" && city.country == "IN") {
                        city.alreadyExist = true
                        break
                    }
                }

                setSearchCitiesAdapter()
                binding.searchPlaceholderTV.visibility = View.GONE
                binding.citiesRecyclerView.visibility = View.VISIBLE
            } else {
                binding.searchPlaceholderTV.text = getString(R.string.no_results)
                binding.searchPlaceholderTV.visibility = View.VISIBLE
                binding.citiesRecyclerView.visibility = View.GONE
            }
        }


        //SearchView Text Listener
        binding.searchCity.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.citiesRecyclerView.visibility = View.GONE

                if(query != null) {
                    binding.locateMe.visibility = View.GONE
                    binding.searchPlaceholderTV.visibility = View.VISIBLE

                    if(query.trim().isEmpty()) {
                        binding.locateMe.visibility = View.VISIBLE
                        binding.searchPlaceholderTV.visibility = View.GONE
                    } else if(query.trim().length <= 2) {
                        binding.searchPlaceholderTV.text = getString(R.string.search_your_city)
                    } else {
                        binding.searchPlaceholderTV.text = getString(R.string.searching)
                        lifecycleScope.launch(Dispatchers.IO) {
                            geoLocationViewModel.getLocation(query, CITY_LIMITS, resources.getString(R.string.api_key))
                        }

                    }
                } else {
                    binding.locateMe.visibility = View.VISIBLE
                    binding.searchPlaceholderTV.visibility = View.GONE
                }

                return false
            }

            override fun onQueryTextChange(newCity: String?): Boolean {
                binding.citiesRecyclerView.visibility = View.GONE

                if(newCity != null) {
                    binding.locateMe.visibility = View.GONE
                    binding.searchPlaceholderTV.visibility = View.VISIBLE

                    if(newCity.trim().isEmpty()) {
                        binding.locateMe.visibility = View.VISIBLE
                        binding.searchPlaceholderTV.visibility = View.GONE
                    } else if(newCity.trim().length <= 2) {
                        binding.searchPlaceholderTV.text = getString(R.string.search_your_city)
                    } else {
                        binding.searchPlaceholderTV.text = getString(R.string.click_search)
                    }
                } else {
                    binding.locateMe.visibility = View.VISIBLE
                    binding.citiesRecyclerView.visibility = View.GONE
                }

                return false
            }
        })

    }

    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

    private fun setSearchCitiesAdapter() {
        binding.citiesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val adapter = SearchCitiesAdapter(citiesList)
        binding.citiesRecyclerView.adapter = adapter
    }

}