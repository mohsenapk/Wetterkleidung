package com.mohsen.apk.wetterkleidung.ui.setting.di

import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.setting.SettingViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SettingModule {

    @ActivityScope
    @Provides
    fun provideSettingViewModelFactory(prefs: InAppSharedPreferenceManager) =
        SettingViewModelFactory(prefs)
}