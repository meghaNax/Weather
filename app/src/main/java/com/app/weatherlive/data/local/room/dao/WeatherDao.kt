package com.app.weatherlive.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.weatherlive.data.remote.beans.WeatherEntity


@Dao
interface WeatherDao {
    @Insert
    fun saveWeatherInfo(weatherEntity: WeatherEntity?)

    @Query("SELECT * FROM WeatherEntity")
    fun getWeatherInfo(): LiveData<WeatherEntity>?

    @Query("DELETE FROM WeatherEntity WHERE id = :id")
    fun deleteWeatherWithKey(id: Long)

    @Query("DELETE FROM WeatherEntity")
    fun deleteAll()
}