package com.mohsen.apk.wetterkleidung.ui.city.di

import com.mohsen.apk.wetterkleidung.base.di.ApplicationComponent
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    modules = [CityModule::class],
    dependencies = [ApplicationComponent::class]
)
interface CityComponent {
    fun inject(target: CityFragment)
}