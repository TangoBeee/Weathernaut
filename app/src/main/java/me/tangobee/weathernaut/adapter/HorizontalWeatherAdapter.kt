package me.tangobee.weathernaut.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.model.WeatherHourlyCardData

class HorizontalWeatherAdapter(private val weatherDataList: List<WeatherHourlyCardData>) :
    Adapter<HorizontalWeatherAdapter.HorizontalWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_time_card_layout, parent, false)
        return HorizontalWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherDataList.size
    }

    override fun onBindViewHolder(holder: HorizontalWeatherViewHolder, position: Int) {
        val currWeatherData = weatherDataList[position]

        if(currWeatherData.isActive) {
            holder.cardParent.setBackgroundResource(R.drawable.active_weather_time_column_style)
            holder.weatherTime.setText(R.string.now)
            holder.weatherTime.setTextColor(Color.parseColor("#303345"))
        } else {
            holder.weatherTime.text = currWeatherData.time
        }

        holder.weatherIcon.setImageResource(currWeatherData.weatherIcon)
        holder.weatherValue.text = currWeatherData.weatherValue
    }


    class HorizontalWeatherViewHolder(itemView: View) : ViewHolder(itemView) {
        val cardParent: ConstraintLayout = itemView.findViewById(R.id.smallWeatherCardParent)
        val weatherTime: TextView = itemView.findViewById(R.id.weatherTime)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        val weatherValue: TextView = itemView.findViewById(R.id.weatherValue)
    }

}