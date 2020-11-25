package com.mohsen.apk.wetterkleidung.base

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.base.di.BaseModule
import com.mohsen.apk.wetterkleidung.base.di.DaggerApplicationComponent
import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.ui.city.di.CityComponent
import com.mohsen.apk.wetterkleidung.ui.city.di.CityModule
import com.mohsen.apk.wetterkleidung.ui.city.di.DaggerCityComponent
import com.mohsen.apk.wetterkleidung.ui.dialog.di.DialogModule
import com.mohsen.apk.wetterkleidung.ui.weather.di.DaggerWeatherComponent
import com.mohsen.apk.wetterkleidung.ui.weather.di.WeatherComponent
import com.mohsen.apk.wetterkleidung.ui.weather.di.WeatherModule
import com.mohsen.apk.wetterkleidung.ui.setting.di.DaggerSettingComponent
import com.mohsen.apk.wetterkleidung.ui.setting.di.SettingComponent
import com.mohsen.apk.wetterkleidung.ui.setting.di.SettingModule
import com.mohsen.apk.wetterkleidung.ui.main.di.DaggerMainComponent
import com.mohsen.apk.wetterkleidung.ui.main.di.MainComponent
import com.mohsen.apk.wetterkleidung.ui.main.di.MainModule
import com.mohsen.apk.wetterkleidung.ui.splash.di.DaggerSplashComponent
import com.mohsen.apk.wetterkleidung.ui.splash.di.SplashComponent
import com.mohsen.apk.wetterkleidung.ui.splash.di.SplashModule
import com.mohsen.apk.wetterkleidung.utility.di.UtilityModule
import timber.log.Timber

class BaseApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var weatherComponent: WeatherComponent
    lateinit var cityComponent: CityComponent
    lateinit var mainComponent: MainComponent
    lateinit var settingComponent: SettingComponent
    lateinit var splashComponent: SplashComponent

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
        initApplicationComponent()
        initSplashComponent()
        initMainComponent()
        initWeatherComponent()
        initCityComponent()
        initSettingComponent()
    }

    private fun initSplashComponent(){
        splashComponent =
            DaggerSplashComponent.builder()
                .splashModule(SplashModule())
                .applicationComponent(applicationComponent)
                .build()
    }

    private fun initSettingComponent() {
        settingComponent =
            DaggerSettingComponent.builder()
                .settingModule(SettingModule())
                .applicationComponent(applicationComponent)
                .dialogModule(DialogModule())
                .build()
    }

    private fun initMainComponent() {
        mainComponent =
            DaggerMainComponent.builder()
                .mainModule(MainModule())
                .applicationComponent(applicationComponent)
                .build()
    }

    private fun initCityComponent() {
        cityComponent =
            DaggerCityComponent.builder()
                .cityModule(CityModule())
                .applicationComponent(applicationComponent)
                .build()
    }

    private fun initWeatherComponent() {
        weatherComponent =
            DaggerWeatherComponent.builder()
                .weatherModule(WeatherModule())
                .applicationComponent(applicationComponent)
                .build()
    }

    private fun initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
            .baseModule(BaseModule(this))
            .networkModule(NetworkModule(this))
            .dBModule(DBModule(this))
            .repositoryModule(RepositoryModule())
            .utilityModule(UtilityModule())
            .build()
    }
}