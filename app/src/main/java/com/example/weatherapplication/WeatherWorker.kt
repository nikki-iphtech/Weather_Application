package com.example.weatherapplication

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope

class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        val city = inputData.getString("CITY") ?: return@coroutineScope Result.failure()

        try {
            val appContext = applicationContext
            val viewModel = WeatherViewModel(appContext as android.app.Application)
            viewModel.fetchWeather(city)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
