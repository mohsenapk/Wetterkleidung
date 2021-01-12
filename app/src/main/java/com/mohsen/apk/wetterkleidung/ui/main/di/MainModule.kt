package com.mohsen.apk.wetterkleidung.ui.main.di

import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.main.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideSplashViewModelFactory(prefs: InAppSharedPreferenceManager) =
        MainViewModelFactory(prefs)
}