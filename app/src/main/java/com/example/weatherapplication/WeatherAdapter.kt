package com.example.weatherapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WeatherAdapter(private var weatherList: List<WeatherEntity>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCityItem: TextView = itemView.findViewById(R.id.tvCityItem)
        private val tvTempItem: TextView = itemView.findViewById(R.id.tvTempItem)
        private val tvConditionItem: TextView = itemView.findViewById(R.id.tvConditionItem)
        private val ivWeatherIconItem: ImageView = itemView.findViewById(R.id.ivWeatherIconItem)

        fun bind(weather: WeatherEntity) {
            tvCityItem.text = "${weather.city}, ${weather.country}"
            tvTempItem.text = "${weather.temperature}°C"
            tvConditionItem.text = weather.condition
            Glide.with(itemView.context).load(weather.icon).into(ivWeatherIconItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size

    fun updateData(newList: List<WeatherEntity>) {
        weatherList = newList
        notifyDataSetChanged()
    }
}
