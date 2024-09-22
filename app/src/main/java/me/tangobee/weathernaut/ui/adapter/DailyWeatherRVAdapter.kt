package me.tangobee.weathernaut.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.models.DailyWeatherRVModel

class DailyWeatherRVAdapter(private val dailyWeatherRVModelList: ArrayList<DailyWeatherRVModel>) : RecyclerView.Adapter<DailyWeatherRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_next_weather_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dailyWeatherRVModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nextWeatherDay.text = dailyWeatherRVModelList[position].day
        holder.nextWeatherTemp.text = dailyWeatherRVModelList[position].weatherTemp
        holder.nextWeatherIcon.setImageResource(dailyWeatherRVModelList[position].weatherIcon)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nextWeatherIcon: ImageView = itemView.findViewById(R.id.nextWeatherIcon)
        val nextWeatherDay: TextView = itemView.findViewById(R.id.nextWeatherDay)
        val nextWeatherTemp: TextView = itemView.findViewById(R.id.nextWeatherTemp)
    }
}