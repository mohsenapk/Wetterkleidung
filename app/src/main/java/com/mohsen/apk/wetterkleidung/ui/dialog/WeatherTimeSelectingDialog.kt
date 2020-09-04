package com.mohsen.apk.wetterkleidung.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.SeekBarValue
import com.mohsen.apk.wetterkleidung.model.TimeSelect
import com.mohsen.apk.wetterkleidung.ui.adapter.TimeSelectingAdapter
import kotlinx.android.synthetic.main.dialog_weather_time_selecting.*
import timber.log.Timber

class WeatherTimeSelectingDialog(
    context: Context
) : Dialog(context) {
    private val rvTimesLayoutManager = LinearLayoutManager(context)
    private var listTimes = mutableListOf(TimeSelect("select all", true))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_weather_time_selecting)
        createTimeList()
        initUi()
    }

    private fun initUi() {
        btnAccept.setOnClickListener {
            if(listTimes.isEmpty())
                return@setOnClickListener
            Timber.d("list-times : \n $listTimes")
            dismiss()
        }
        btnCancel.setOnClickListener { dismiss() }
        rvTimes.apply {
            layoutManager = rvTimesLayoutManager
            adapter = TimeSelectingAdapter(listTimes) {
                listTimes = it.toMutableList()
            }
        }
    }

    private fun createTimeList() {
        SeekBarValue.values().toList().forEach {
            listTimes.add(TimeSelect(it.hours, true))
        }
    }

}