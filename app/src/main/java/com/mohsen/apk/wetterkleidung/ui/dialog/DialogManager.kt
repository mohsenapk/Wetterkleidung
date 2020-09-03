package com.mohsen.apk.wetterkleidung.ui.dialog

import android.content.Context

interface DialogManager {
    fun showWeatherTimeSelectingDialog(context: Context)
}

class DialogManagerImpl() : DialogManager {

    override fun showWeatherTimeSelectingDialog(context: Context) {
        WeatherTimeSelectingDialog(context).show()
    }
}