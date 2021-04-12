@file:Suppress("DEPRECATION")

package com.app.weatherlive.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*


class GPSLocationFetcher {

    companion object{
        const val TAG: String ="GPSLocationFetcher"
    }
    private val permissionRequestCode: Int = 0x12

    interface GPSCoordinateInterface {
        fun loadLatLong(lattitude: Double, longitude: Double)
    }

    interface GPSStateInterface {
        fun gpsEnabled()
    }

    fun checkPermissions(context: Activity, fusedLocationClient: FusedLocationProviderClient, listener: GPSCoordinateInterface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                getLocation(fusedLocationClient, listener)
            } else {
                requestPermissions(context)
            }
        } else {
            getLocation(fusedLocationClient, listener)
        }

    }
    fun checkPermissions(context: Context, fusedLocationClient: FusedLocationProviderClient, listener: GPSCoordinateInterface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                getLocation(fusedLocationClient, listener)
            }
        } else {
            getLocation(fusedLocationClient, listener)
        }

    }


    @SuppressLint("MissingPermission")
    private fun getLocation(locationClient: FusedLocationProviderClient,
                            gpsCoordinateInterface: GPSCoordinateInterface) {
        locationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        gpsCoordinateInterface.loadLatLong(location.latitude, location.longitude)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("GPSLocationFetcher", "Failed to get GPS location")
                    e.printStackTrace()
                }

    }


    fun retrieveLocation(context: Activity,
                         fusedLocationClient: FusedLocationProviderClient,
                         listener: GPSCoordinateInterface
    ) {

        showLocationRequest(context, object : GPSStateInterface {
            override fun gpsEnabled() {
                checkPermissions(context, fusedLocationClient, listener)
            }

        })


    }

    fun retrieveLocation(context: Context,
                         fusedLocationClient: FusedLocationProviderClient,
                         listener: GPSCoordinateInterface
    ) {

        showLocationRequest(context, object : GPSStateInterface {
            override fun gpsEnabled() {
                checkPermissions(context, fusedLocationClient, listener)
            }

        })


    }

    private fun requestPermissions(context: Activity) {
        ActivityCompat.requestPermissions(context,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), permissionRequestCode)
    }


    private fun showLocationRequest(context: Context,
                                    gpsStateInterface: GPSStateInterface) {
        val googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = (10000 / 2).toLong()
        }
        val locBuilder = LocationSettingsRequest.Builder().addLocationRequest(locRequest).apply {
            this.setAlwaysShow(true)
        }

        val locResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locBuilder.build())
        locResult.setResultCallback { mResult ->
            val mStatus = mResult.status
            when (mStatus.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.i(TAG, "All location settings are satisfied.")
                    gpsStateInterface.gpsEnabled()
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    Log.i(
                        TAG,
                        "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                    )
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )

                    try {
                        mStatus.startResolutionForResult(context as Activity, 12)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }

                }
            }
        }
    }


}