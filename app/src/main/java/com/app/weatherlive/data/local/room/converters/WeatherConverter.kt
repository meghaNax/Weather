package com.app.weatherlive.data.local.room.converters

import androidx.room.TypeConverter
import com.app.weatherlive.data.remote.beans.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherConverter {
    private val gSon = Gson()

    @TypeConverter
    fun stringToList(data: String?): ArrayList<Weather> {
        return if (data == null) {
            ArrayList()
        }else{
            val listType =
                object : TypeToken<ArrayList<Weather?>?>() {}.type
            gSon.fromJson(
                data,
                listType
            ) ?: ArrayList()
        }
    }

    @TypeConverter
    fun listToString(someObjects: ArrayList<Weather?>?): String {
        return gSon.toJson(someObjects)
    }
}
