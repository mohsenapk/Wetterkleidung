package com.mohsen.apk.wetterkleidung.ui.weather.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [WeatherModule::class],
    dependencies = [ApplicationComponent::class]
)
interface WeatherComponent {
    fun inject(target: WeatherFragment)
}