package me.tangobee.weathernaut.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.models.GeocodingData.GeocodingResult
import me.tangobee.weathernaut.utils.GeocodingHelper

class LocationRVAdapter(private val locationRVModalList: ArrayList<GeocodingResult>, private val lat: Double, private val long: Double) : RecyclerView.Adapter<LocationRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cities_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return locationRVModalList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = locationRVModalList[position].name
        val cityAddress = listOf(
            locationRVModalList[position].admin4,
            locationRVModalList[position].admin3,
            locationRVModalList[position].admin2,
            locationRVModalList[position].admin1,
            locationRVModalList[position].country
        ).filterNot { it.isNullOrBlank() }.joinToString(", ")

        holder.cityAddress.text = cityAddress

        holder.addRemoveCity.setOnClickListener {
            holder.addRemoveCity.setImageResource(R.drawable.icon_right_arrow)
        }

        val samePlace = GeocodingHelper.areLocationsSame(lat, long, locationRVModalList[position].latitude, locationRVModalList[position].longitude)

        if(samePlace) {
            holder.addRemoveCity.setImageResource(R.drawable.icon_right_arrow)
        } else {
            holder.addRemoveCity.setImageResource(R.drawable.icon_add)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityName)
        val cityAddress: TextView = itemView.findViewById(R.id.cityAddress)
        val addRemoveCity: ImageButton = itemView.findViewById(R.id.addRemoveCity)
    }
}