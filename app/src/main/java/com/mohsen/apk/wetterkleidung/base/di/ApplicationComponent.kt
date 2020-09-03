package com.mohsen.apk.wetterkleidung.base.di

import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.repository.WeatherRepositoryImpl
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogManager
import com.mohsen.apk.wetterkleidung.ui.dialog.di.DialogModule
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelperImpl
import com.mohsen.apk.wetterkleidung.utility.LocationHelper
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
    fun getSharedPreferenceManager(): SharedPreferenceManager
    fun getApplication(): BaseApplication
    fun getLocationHelper(): LocationHelper
}