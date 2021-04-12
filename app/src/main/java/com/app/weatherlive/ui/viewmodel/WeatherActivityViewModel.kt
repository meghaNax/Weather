package com.app.weatherlive.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.weatherlive.api_service.ApiInfo
import com.app.weatherlive.data.WeatherRepository
import com.app.weatherlive.data.remote.beans.WeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WeatherActivityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
    ): ViewModel() {

    private val TAG = javaClass.simpleName

    private val _wList: MutableLiveData<WeatherEntity> = MutableLiveData<WeatherEntity>()
    val wList: LiveData<WeatherEntity>
        get() = _wList

    fun fetchWeatherData(latitude: Double,
                         longitude: Double,
                         apiKey: String= ApiInfo.API_KEY) {
        weatherRepository.getLiveWeather(latitude, longitude, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                run {
                    _wList.value = result
                }
            }
            ) { error ->
                Log.e(TAG, "ERROR: " + error.message)
            }
    }

    fun insertWeather(weatherEntity: WeatherEntity?) {
        weatherRepository.saveWeatherInfo(weatherEntity)
    }

    fun deleteWeather(name: Long) {
        weatherRepository.deleteWeather(name)
    }

    fun fetchWeatherList(): LiveData<WeatherEntity> {
        return wList
    }


}