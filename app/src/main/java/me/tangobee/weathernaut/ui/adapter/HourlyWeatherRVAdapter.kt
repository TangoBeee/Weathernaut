package me.tangobee.weathernaut.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.models.HourlyWeatherRVModel

class HourlyWeatherRVAdapter(private val hourlyWeatherRVModelList: ArrayList<HourlyWeatherRVModel>) : RecyclerView.Adapter<HourlyWeatherRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hourly_weather_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.hourlyWeatherContainer.context
        holder.weatherIcon.setImageResource(hourlyWeatherRVModelList[position].weatherIcon)
        holder.weatherValue.text = hourlyWeatherRVModelList[position].weatherTemp

        val time = hourlyWeatherRVModelList[position].time
        if(time == "now") {
            val boldTypeface = ResourcesCompat.getFont(context, R.font.inter_semibold)
            holder.weatherTime.setTextColor(ContextCompat.getColor(context, R.color.textColor))
            holder.weatherTime.typeface = boldTypeface
            holder.hourlyWeatherContainer.background = ContextCompat.getDrawable(context, R.drawable.current_hourly_weather_background)
        } else {
            val typeface = ResourcesCompat.getFont(context, R.font.inter)
            holder.weatherTime.setTextColor(ContextCompat.getColor(context, R.color.upcomingTimeColor))
            holder.weatherTime.typeface = typeface
            holder.hourlyWeatherContainer.background = ContextCompat.getDrawable(context, R.drawable.hourly_weather_box_background)
        }

        holder.weatherTime.text = time
    }

    override fun getItemCount(): Int {
        return hourlyWeatherRVModelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hourlyWeatherContainer: LinearLayout = itemView.findViewById(R.id.hourlyWeatherContainer)
        val weatherTime: TextView = itemView.findViewById(R.id.weatherTime)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        val weatherValue: TextView = itemView.findViewById(R.id.weatherValue)
    }
}