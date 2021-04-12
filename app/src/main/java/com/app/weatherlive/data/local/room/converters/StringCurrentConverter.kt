package com.app.weatherlive.data.local.room.converters

import androidx.room.TypeConverter
import com.app.weatherlive.data.remote.beans.Current
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringCurrentConverter {

    private val gSon = Gson()

    @TypeConverter
    fun stringToCurrent(data: String?): Current {
        if (data == null) {
            return Current()
        }
        val listType =
            object : TypeToken<Current?>() {}.type
        return gSon.fromJson(
            data,
            listType
        ) ?: Current()
    }

    @TypeConverter
    fun currentToString(someObjects: Current?): String {
        return gSon.toJson(someObjects)
    }
}