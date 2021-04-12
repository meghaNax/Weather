package com.app.weatherlive.data

import androidx.lifecycle.LiveData
import com.app.weatherlive.data.local.room.dao.WeatherDao
import com.app.weatherlive.data.remote.beans.WeatherEntity
import com.app.weatherlive.api_service.WeatherService
import io.reactivex.rxjava3.core.Observable

import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherService: WeatherService) {

    fun getLiveWeather(lat: Double, long: Double, apiKey : String): Observable<WeatherEntity> {
        return weatherService.getWeather(lat,long,"minutely,daily,alerts", "imperial" ,apiKey)
    }

    fun saveWeatherInfo(weatherEntity: WeatherEntity?) {
        weatherDao.saveWeatherInfo(weatherEntity)
    }

    fun deleteWeather(id: Long) {
        weatherDao.deleteWeatherWithKey(id)
    }

    fun deleteAll() {
        weatherDao.deleteAll()
    }

}