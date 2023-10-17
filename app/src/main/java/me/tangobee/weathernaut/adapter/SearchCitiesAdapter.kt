package me.tangobee.weathernaut.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.model.CityLocationData
import me.tangobee.weathernaut.ui.liveDate.SearchCitiesLiveData
import me.tangobee.weathernaut.util.CountryNameByCode


class SearchCitiesAdapter(private val citiesList: CityLocationData) : Adapter<SearchCitiesAdapter.SearchCitiesViewHolder>() {

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

        val location = citiesList[position].state + ", " + CountryNameByCode.getCountryNameByCode(context, citiesList[position].country)
        holder.cityAddress.text = location

        if(citiesList[position].alreadyExist) {
            holder.alreadyAdded.setImageResource(R.drawable.icon_right_arrow)
        } else {
            holder.alreadyAdded.setImageResource(R.drawable .icon_add)
        }

        holder.alreadyAdded.setOnClickListener {
            if(citiesList[position].alreadyExist) {
                citiesList[position].alreadyExist = !citiesList[position].alreadyExist
                SearchCitiesLiveData.updateCitiesLiveData(citiesList[position])
                (context as Activity).finish()
            } else {
                holder.alreadyAdded.setImageResource(R.drawable.icon_right_arrow)
                for(i in 0 until itemCount) {
                    if(citiesList[i].alreadyExist) {
                        citiesList[i].alreadyExist = !citiesList[i].alreadyExist
                        notifyItemChanged(i)
                        break
                    }
                }

                SearchCitiesLiveData.updateCitiesLiveData(citiesList[position])
                citiesList[position].alreadyExist = !citiesList[position].alreadyExist
            }

            notifyItemChanged(position)
        }
    }

    inner class SearchCitiesViewHolder(itemView: View) : ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityName)
        val cityAddress: TextView = itemView.findViewById(R.id.cityAddress)
        val alreadyAdded: ImageButton = itemView.findViewById(R.id.addRemoveCity)
    }

}