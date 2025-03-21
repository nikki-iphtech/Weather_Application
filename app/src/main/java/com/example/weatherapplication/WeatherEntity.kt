package com.example.weatherapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey val city: String,
    val country: String,
    val temperature: Double,
    val condition: String,
    val icon: String
)
