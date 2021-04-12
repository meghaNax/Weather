package com.app.weatherlive.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.weatherlive.data.remote.beans.Hourly
import com.app.weatherlive.databinding.ActivityWeatherInfoBinding
import com.app.weatherlive.ui.adapter.LiveWeatherAdapter
import com.app.weatherlive.util.GPSLocationFetcher
import com.app.weatherlive.ui.viewmodel.WeatherActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherInfoActivity : AppCompatActivity(), GPSLocationFetcher.GPSCoordinateInterface {

    private val binding: ActivityWeatherInfoBinding by lazy {
        ActivityWeatherInfoBinding.inflate(layoutInflater)
    }

    private val weatherActivityActivityViewModel: WeatherActivityViewModel by viewModels()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val liveWeatherAdapter: LiveWeatherAdapter
    get() = binding.weatherRecyclerView.adapter as LiveWeatherAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initUI()
        GPSLocationFetcher().retrieveLocation(this, fusedLocationClient, this)
        observeWeather()
    }

    private fun observeWeather() {
        weatherActivityActivityViewModel.fetchWeatherList().observe(this, Observer{it.hourly?.let {list->liveWeatherAdapter.updateList(list)}})
    }

    override fun loadLatLong(lattitude: Double, longitude: Double) {
        weatherActivityActivityViewModel.fetchWeatherData(lattitude, longitude)
    }

    private fun initUI() {
        binding.weatherRecyclerView.adapter = LiveWeatherAdapter(this)
    }
}