package com.mohsen.apk.wetterkleidung.base

import android.app.Application
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.base.di.DaggerApplicationComponent
import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.ui.di.DaggerMainComponent
import com.mohsen.apk.wetterkleidung.ui.di.MainComponent
import com.mohsen.apk.wetterkleidung.ui.di.MainModule
import timber.log.Timber

class BaseApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var mainComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
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
            .build()

        mainComponent = DaggerMainComponent.builder()
            .mainModule(MainModule())
            .applicationComponent(applicationComponent)
            .build()
    }
}