package com.example.weatherapplication

import androidx.room.*

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table WHERE city = :city LIMIT 1")
    suspend fun getWeather(city: String): WeatherEntity?

    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    suspend fun getAllWeatherHistory(): List<WeatherEntity>
}
