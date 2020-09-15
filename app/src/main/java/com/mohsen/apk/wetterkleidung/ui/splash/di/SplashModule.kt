package com.mohsen.apk.wetterkleidung.ui.splash.di

import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.splash.SplashViewModel
import com.mohsen.apk.wetterkleidung.ui.splash.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    @ActivityScope
    fun provideSplashViewModelFactory(prefs: SharedPreferenceManager) =
        SplashViewModelFactory(prefs)
}