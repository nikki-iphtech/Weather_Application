package com.example.weatherapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvWeatherHistory = findViewById<RecyclerView>(R.id.rvWeatherHistory)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val etCity = findViewById<EditText>(R.id.etCity)
        val tvCity = findViewById<TextView>(R.id.tvCity)
        val tvTemp = findViewById<TextView>(R.id.tvTemp)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)
        val ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)


        adapter = WeatherAdapter(emptyList())
        rvWeatherHistory.layoutManager = LinearLayoutManager(this)
        rvWeatherHistory.adapter = adapter


        btnSearch.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                viewModel.fetchWeather(city)
                scheduleWeatherUpdate(city)
            }
        }


        viewModel.weather.observe(this, Observer { weather ->
            weather?.let {
                tvCity.text = "${it.city}, ${it.country}"
                tvTemp.text = "${it.temperature}°C"
                tvCondition.text = it.condition
                Glide.with(this).load(it.icon).into(ivWeatherIcon)
            }
        })


        viewModel.weatherHistory.observe(this, Observer { weatherList ->
            adapter.updateData(weatherList)
        })
    }


    private fun scheduleWeatherUpdate(city: String) {
        val workManager = WorkManager.getInstance(this)

        val inputData = workDataOf("CITY" to city)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<WeatherWorker>(6, TimeUnit.HOURS)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "WeatherUpdateWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
