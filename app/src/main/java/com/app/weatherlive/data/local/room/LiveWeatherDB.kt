package com.app.weatherlive.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.weatherlive.data.local.room.dao.WeatherDao
import com.app.weatherlive.data.remote.beans.WeatherEntity

@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = true)
abstract class LiveWeatherDB: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object{
        const val DB_NAME="LiveWeather"
    }
}