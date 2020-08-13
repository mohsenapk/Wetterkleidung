package com.mohsen.apk.wetterkleidung.ui.dialog.di

import android.content.Context
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogShowingManager
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogShowingManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DialogShowingModule() {

    @ActivityScope
    @Provides
    fun provideDialogShowingManager(): DialogShowingManager =
        DialogShowingManagerImpl()
}