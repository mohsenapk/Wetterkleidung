package com.mohsen.apk.wetterkleidung.ui.main.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [MainModule::class],
    dependencies = [ApplicationComponent::class]
)
interface MainComponent {
    fun inject(target: MainActivity)
}