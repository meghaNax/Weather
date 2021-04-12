package com.app.weatherlive.di

import android.app.Application
import androidx.room.Room
import com.app.weatherlive.data.local.room.LiveWeatherDB
import com.app.weatherlive.data.local.room.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LiveWeatherDBModule {

    @Provides
    @Singleton
    fun provideWeatherDB(application: Application?): LiveWeatherDB {
        return Room.databaseBuilder(application!!, LiveWeatherDB::class.java, LiveWeatherDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(liveWeatherDB: LiveWeatherDB): WeatherDao {
        return liveWeatherDB.weatherDao()
    }

}