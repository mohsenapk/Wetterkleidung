package com.mohsen.apk.wetterkleidung.ui.city.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.city.CityActivity
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogShowingManager
import com.mohsen.apk.wetterkleidung.ui.dialog.di.DialogShowingModule
import dagger.Component

@ActivityScope
@Component(
    modules = [CityModule::class,
        DialogShowingModule::class],
    dependencies = [ApplicationComponent::class]
)
interface CityComponent {
    fun inject(target: CityActivity)
}