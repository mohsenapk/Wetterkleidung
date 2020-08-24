package com.mohsen.apk.wetterkleidung.ui.setting.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.setting.SettingActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [SettingModule::class],
    dependencies = [ApplicationComponent::class]
)
interface SettingComponent {
    fun inject(target: SettingActivity)
}