package com.mohsen.apk.wetterkleidung.ui.main.di

import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.main.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideSplashViewModelFactory(prefs: SharedPreferenceManager) =
        MainViewModelFactory(prefs)
}