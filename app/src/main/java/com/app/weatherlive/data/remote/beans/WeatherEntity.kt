package com.app.weatherlive.data.remote.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.weatherlive.data.local.room.converters.StringCurrentConverter
import com.app.weatherlive.data.local.room.converters.StringHourConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
public class WeatherEntity {
    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lon")
    @Expose
    var lon: Double? = null

    @SerializedName("timezone")
    @Expose
    var timezone: String? = null

    @SerializedName("timezone_offset")
    @Expose
    var timezoneOffset: Int? = null

    @SerializedName("current")
    @Expose
    @TypeConverters(StringCurrentConverter::class)
    var current: Current? = null

    @SerializedName("hourly")
    @Expose
    @TypeConverters(StringHourConverter::class)
    var hourly: List<Hourly>? = null

    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
