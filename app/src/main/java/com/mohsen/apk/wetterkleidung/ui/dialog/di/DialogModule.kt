package com.mohsen.apk.wetterkleidung.ui.dialog.di

import android.content.Context
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogManager
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogManagerImpl
import dagger.Module
import dagger.Provides

@Module
class DialogModule() {

    @ActivityScope
    @Provides
    fun provideDialogManager(): DialogManager =
        DialogManagerImpl()
}