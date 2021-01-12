package com.mohsen.apk.wetterkleidung.base.di

import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.utility.*
import com.mohsen.apk.wetterkleidung.utility.di.UtilityModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        BaseModule::class,
        NetworkModule::class,
        DBModule::class,
        RepositoryModule::class,
        UtilityModule::class]
)
@Singleton
interface ApplicationComponent {
    fun getRepository(): WeatherRepository
    fun getDateHelper(): DateHelper
    fun getImageHelper(): ImageHelper
    fun getSharedPreferenceManager(): InAppSharedPreferenceManager
    fun getApplication(): BaseApplication
    fun getLocationHelper(): LocationHelper
    fun getDayNameManager(): DayNameManager
    fun getResourceManager(): ResourceManager
    fun getSeekBarManager(): SeekBarManager
}