package com.mohsen.apk.wetterkleidung.ui.splash.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [SplashModule::class],
    dependencies = [ApplicationComponent::class]
)
interface SplashComponent {
    fun inject(target: SplashActivity)
}