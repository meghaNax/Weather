package com.app.weatherlive.ui.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.app.weatherlive.util.GPSLocationFetcher
import com.app.weatherlive.di.WeatherServiceModule
import com.app.weatherlive.R
import com.app.weatherlive.api_service.ApiInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LiveWeatherService : Service(), GPSLocationFetcher.GPSCoordinateInterface {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        this.getData()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        GPSLocationFetcher().retrieveLocation(applicationContext, fusedLocationClient!!, this)
    }

    override fun loadLatLong(lattitude: Double, longitude: Double) {
        WeatherServiceModule.provideWeatherService()
            .getWeather(
                lattitude,
                longitude,
                "minutely,daily,alerts",
                "imperial",
                ApiInfo.API_KEY
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                run {
                    val view = RemoteViews(packageName, R.layout.widget_live_weather)
                    view.setTextViewText(R.id.txtLiveWeather, result.current?.temp.toString())
                    val theWidget = ComponentName(this, LiveWeatherWidget::class.java)
                    val manager = AppWidgetManager.getInstance(this)
                    manager.updateAppWidget(theWidget, view)
                }
            }
            ) { error -> Log.e("", "weather: " + error.message) }
    }
}