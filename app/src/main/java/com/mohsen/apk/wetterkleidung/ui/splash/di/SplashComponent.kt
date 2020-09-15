package com.mohsen.apk.wetterkleidung.ui.splash.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.splash.SplashFragment
import com.mohsen.apk.wetterkleidung.ui.splash.SplashViewModel
import dagger.Component

@ActivityScope
@Component(
    modules = [SplashModule::class],
    dependencies = [ApplicationComponent::class]
)
interface SplashComponent {
    fun inject(target: SplashFragment)
}