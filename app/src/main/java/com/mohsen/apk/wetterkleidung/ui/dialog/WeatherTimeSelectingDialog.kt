package com.mohsen.apk.wetterkleidung.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.SeekBarValue
import com.mohsen.apk.wetterkleidung.ui.adapter.TimeSelectingAdapter
import kotlinx.android.synthetic.main.dialog_weather_time_selecting.*

class WeatherTimeSelectingDialog(
    context: Context
) : Dialog(context) {
    private val rvTimesLayoutManager = LinearLayoutManager(context)
    private val  listStrTimes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_weather_time_selecting)
        createTimeList()
        initUi()
    }

    private fun initUi() {
        rvTimes.apply {
            layoutManager = rvTimesLayoutManager
            adapter = TimeSelectingAdapter(listStrTimes)
        }
    }

    private fun createTimeList() {
        SeekBarValue.values().toList().forEach {
            listStrTimes.add(it.hours)
        }
    }
    
}