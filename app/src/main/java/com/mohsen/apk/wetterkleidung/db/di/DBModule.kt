package com.mohsen.apk.wetterkleidung.db.di

import android.content.Context
import androidx.room.Room
import com.mohsen.apk.wetterkleidung.db.WeatherDB
import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalServiceImpl
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideDB(): WeatherDB =
        Room.databaseBuilder(
            context,
            WeatherDB::class.java,
            "weather_db"
        ).build()

    @Provides
    @Singleton
    fun provideWeatherLocalService(db: WeatherDB) =
        WeatherLocalServiceImpl(db)

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferenceManager =
        SharedPreferenceManagerImpl(context)

}