package com.mohsen.apk.wetterkleidung.ui.splash.di

import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.splash.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @ActivityScope
    @Provides
    fun provideSplashViewModelFactory(prefs: SharedPreferenceManager) =
        SplashViewModelFactory(prefs)
}