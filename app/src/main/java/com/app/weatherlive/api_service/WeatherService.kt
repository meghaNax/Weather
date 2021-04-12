package com.app.weatherlive.api_service

import com.app.weatherlive.data.remote.beans.WeatherEntity
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(ApiEndPoints.GET_WEATHER)
    fun getWeather(@Query(ApiParams.LAT) uid: Double,
                   @Query(ApiParams.LONG) latitude: Double,
                   @Query(ApiParams.EXCLUDE) exclude: String? = "minutely,daily,alerts",
                   @Query(ApiParams.UNITS) units: String? = "imperial",
                   @Query(ApiParams.APP_ID) appid: String) : Observable<WeatherEntity>
}