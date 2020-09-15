package com.mohsen.apk.wetterkleidung.ui.dialog

import android.content.Context
import com.mohsen.apk.wetterkleidung.model.TimeSelect

interface DialogManager {
    fun showWeatherTimeSelectingDialog(
        context: Context,
        selectedList: List<TimeSelect>,
        backList: (list: List<TimeSelect>) -> Unit
    )
}

class DialogManagerImpl() : DialogManager {

    override fun showWeatherTimeSelectingDialog(
        context: Context,
        selectedList: List<TimeSelect>,
        backList: (list: List<TimeSelect>) -> Unit
    ) {
        WeatherTimeSelectingDialog(context, selectedList, backList).show()
    }
}