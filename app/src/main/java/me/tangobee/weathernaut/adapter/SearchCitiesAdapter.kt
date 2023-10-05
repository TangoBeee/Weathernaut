package me.tangobee.weathernaut.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.model.LocationData


class SearchCitiesAdapter(private val citiesList: List<LocationData>) : Adapter<SearchCitiesAdapter.SearchCitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCitiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cities_item_layout, parent, false)
        return SearchCitiesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    override fun onBindViewHolder(holder: SearchCitiesViewHolder, position: Int) {

        val context: Context = holder.cityName.context

        holder.cityName.text = citiesList[position].name

        val location = citiesList[position].state + ", " + getCountryNameByCode(citiesList[position].countryCode, context)
        holder.cityAddress.text = location

        if(citiesList[position].alreadyExist) {
            holder.alreadyAdded.setImageResource(R.drawable.icon_right_arrow)
        }
    }

    private fun getCountryNameByCode(countryCode: String, context: Context) : String {
        val resourceName = "country_name_$countryCode"
        val countryNameID = context.resources.getIdentifier(resourceName, "string", context.packageName)

        if(countryNameID != 0) {
            return context.getString(countryNameID)
        }

        return countryCode
    }

    class SearchCitiesViewHolder(itemView: View) : ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityName)
        val cityAddress: TextView = itemView.findViewById(R.id.cityAddress)
        val alreadyAdded: ImageButton = itemView.findViewById(R.id.addRemoveCity)
    }

}