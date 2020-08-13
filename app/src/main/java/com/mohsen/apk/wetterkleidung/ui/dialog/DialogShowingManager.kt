package com.mohsen.apk.wetterkleidung.ui.dialog

import android.app.Activity
import android.content.Context
import android.widget.Toast
import timber.log.Timber

interface DialogShowingManager {
    fun showAddCityDialog(activity: Activity)
}

class DialogShowingManagerImpl() : DialogShowingManager {
    override fun showAddCityDialog(activity: Activity) {

    }
}