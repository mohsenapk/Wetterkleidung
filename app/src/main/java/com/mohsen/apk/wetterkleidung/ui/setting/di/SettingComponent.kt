package com.mohsen.apk.wetterkleidung.ui.setting.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.setting.SettingFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [SettingModule::class],
    dependencies = [ApplicationComponent::class]
)
interface SettingComponent {
    fun inject(target: SettingFragment)
}