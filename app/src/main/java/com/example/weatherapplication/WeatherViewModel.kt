package com.example.weatherapplication

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val database = WeatherDatabase.getDatabase(application)
    private val weatherDao = database.weatherDao()

    private val _weather = MutableLiveData<WeatherEntity?>()
    val weather: LiveData<WeatherEntity?> get() = _weather

    private val _weatherHistory = MutableLiveData<List<WeatherEntity>>()
    val weatherHistory: LiveData<List<WeatherEntity>> get() = _weatherHistory

    init {
        viewModelScope.launch {
            _weatherHistory.postValue(weatherDao.getAllWeatherHistory())
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getCurrentWeather("5ca5321d7a954293bd995836251903", city)
                val weatherEntity = WeatherEntity(
                    city = response.location.name,
                    country = response.location.country,
                    temperature = response.current.temp_c,
                    condition = response.current.condition.text,
                    icon = "https:${response.current.condition.icon}"
                )

                weatherDao.insertWeather(weatherEntity)
                _weather.postValue(weatherEntity)


                _weatherHistory.postValue(weatherDao.getAllWeatherHistory())
            } catch (e: Exception) {
                _weather.postValue(weatherDao.getWeather(city))
            }
        }
    }
}
