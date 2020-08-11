package com.mohsen.apk.wetterkleidung.ui.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.MainActivity
import com.mohsen.apk.wetterkleidung.ui.MainViewModel
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import dagger.Component

@ActivityScope
@Component(
    modules = [MainModule::class],
    dependencies = [ApplicationComponent::class]
)
interface MainComponent {
    fun inject(target: MainActivity)
}