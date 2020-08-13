package com.mohsen.apk.wetterkleidung.base

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.base.di.DaggerApplicationComponent
import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.ui.city.di.CityComponent
import com.mohsen.apk.wetterkleidung.ui.city.di.CityModule
import com.mohsen.apk.wetterkleidung.ui.city.di.DaggerCityComponent
import com.mohsen.apk.wetterkleidung.ui.dialog.di.DialogShowingModule
import com.mohsen.apk.wetterkleidung.ui.main.di.DaggerMainComponent
import com.mohsen.apk.wetterkleidung.ui.main.di.MainComponent
import com.mohsen.apk.wetterkleidung.ui.main.di.MainModule
import com.mohsen.apk.wetterkleidung.utility.di.UtilityModule
import timber.log.Timber

class BaseApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var mainComponent: MainComponent
    lateinit var cityComponent: CityComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initTimber()
        initDagger()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun initDagger() {

        applicationComponent = DaggerApplicationComponent.builder()
            .networkModule(NetworkModule(this))
            .dBModule(DBModule(this))
            .repositoryModule(RepositoryModule())
            .utilityModule(UtilityModule())
            .build()

        mainComponent =
            DaggerMainComponent.builder()
                .mainModule(MainModule())
                .applicationComponent(applicationComponent)
                .build()

        cityComponent =
            DaggerCityComponent.builder()
                .cityModule(CityModule())
                .dialogShowingModule(DialogShowingModule())
                .applicationComponent(applicationComponent)
                .build()
    }
}