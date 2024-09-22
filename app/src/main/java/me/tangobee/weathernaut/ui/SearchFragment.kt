package me.tangobee.weathernaut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineExceptionHandler
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.data.RetrofitHelper
import me.tangobee.weathernaut.data.api.GeocodingService
import me.tangobee.weathernaut.data.repository.GeocodingRepository
import me.tangobee.weathernaut.databinding.FragmentSearchBinding
import me.tangobee.weathernaut.models.GeocodingData.GeocodingModel
import me.tangobee.weathernaut.models.GeocodingData.GeocodingResult
import me.tangobee.weathernaut.models.NewLocationModel
import me.tangobee.weathernaut.ui.adapter.LocationRVAdapter
import me.tangobee.weathernaut.utils.GeocodingHelper
import me.tangobee.weathernaut.viewmodels.GeocodingViewModel
import me.tangobee.weathernaut.viewmodels.GeocodingViewModelFactory
import me.tangobee.weathernaut.viewmodels.WeatherViewModel
import java.net.UnknownHostException
import kotlin.system.exitProcess

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private lateinit var geocodingViewModel: GeocodingViewModel
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler
    private lateinit var mainContainerView: ScrollView

    private var locationRVModelList: ArrayList<GeocodingResult> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainContainerView = requireActivity().findViewById(R.id.mainContainerView)
        mainContainerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        val geocodingService = RetrofitHelper.getInstance().create(GeocodingService::class.java)
        val geocodingRepository = GeocodingRepository(geocodingService)
        geocodingViewModel = ViewModelProvider(requireActivity(), GeocodingViewModelFactory(geocodingRepository))[GeocodingViewModel::class.java]

        coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable is UnknownHostException) {
                Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show()
                exitProcess(0)
            }
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherData ->
            if(weatherData != null) {
                lat = weatherData.lat
                long = weatherData.long
            }
        }

        binding.cancelSearch.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.searchCity.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(newLocationQuery: String?): Boolean {
                if (newLocationQuery.isNullOrEmpty()) {
                    showPlaceholder(getString(R.string.search_your_city))
                } else if (newLocationQuery.length <= 2) {
                    showPlaceholder(getString(R.string.no_locations_found))
                } else {
                    fetchLocation(newLocationQuery)
                }
                return true
            }

            override fun onQueryTextChange(locationQuery: String?): Boolean {
                if (locationQuery.isNullOrEmpty()) {
                    showPlaceholder(getString(R.string.search_your_city))
                }
                return true
            }
        })

        observeViewModel()
    }

    private fun fetchLocation(newLocationQuery: String) {
        showPlaceholder(getString(R.string.loading))
        val newLocationDataModel = NewLocationModel(newLocationQuery)
        geocodingViewModel.getLocations(newLocationDataModel, coroutineExceptionHandler)
    }

    private fun observeViewModel() {
        geocodingViewModel.locationLiveData.observe(viewLifecycleOwner) { locationData ->
            if (locationData == null) {
                showPlaceholder(getString(R.string.something_went_wrong))
                Toast.makeText(requireContext(), getString(R.string.api_fetching_error), Toast.LENGTH_SHORT).show()
            } else if (locationData.results.isNullOrEmpty()) {
                showPlaceholder(getString(R.string.no_locations_found))
            } else {
                setLocationRecyclerViewUIData(locationData)
            }
        }
    }

    private fun setLocationRecyclerViewUIData(locationData: GeocodingModel) {
        locationRVModelList = GeocodingHelper.sortLocationsByProximity(
                lat,
                long,
                locationData.results ?: ArrayList()
        ).toCollection(ArrayList())

        if (locationRVModelList.isEmpty()) {
            showPlaceholder(getString(R.string.no_locations_found))
        } else {
            binding.citiesRecyclerView.visibility = View.VISIBLE
            binding.searchPlaceholderTV.visibility = View.GONE

            val locationRVLinearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            binding.citiesRecyclerView.setHasFixedSize(true)
            binding.citiesRecyclerView.layoutManager = locationRVLinearLayoutManager
            val locationAdapter = LocationRVAdapter(locationRVModelList, lat, long, coroutineExceptionHandler, weatherViewModel)
            binding.citiesRecyclerView.adapter = locationAdapter
        }
    }

    private fun showPlaceholder(message: String) {
        binding.searchPlaceholderTV.text = message
        binding.citiesRecyclerView.visibility = View.GONE
        binding.searchPlaceholderTV.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        showPlaceholder(getString(R.string.search_your_city))
    }

    override fun onDestroy() {
        super.onDestroy()
        mainContainerView.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_background)
        if(locationRVModelList.isNotEmpty()) {
            locationRVModelList.clear()
        }
    }
}