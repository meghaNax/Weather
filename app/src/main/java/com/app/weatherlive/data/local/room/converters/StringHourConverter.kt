package com.app.weatherlive.data.local.room.converters

import androidx.room.TypeConverter
import com.app.weatherlive.data.remote.beans.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringHourConverter {

    private val gSon = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Hourly> {
        return if (data == null) {
            ArrayList()
        }else{
            val listType =
                object : TypeToken<List<Hourly?>?>() {}.type
            gSon.fromJson(
                data,
                listType
            ) ?: ArrayList()
        }
    }

    @TypeConverter
    fun listToString(someObjects: List<Hourly?>?): String {
        return gSon.toJson(someObjects)
    }
}



