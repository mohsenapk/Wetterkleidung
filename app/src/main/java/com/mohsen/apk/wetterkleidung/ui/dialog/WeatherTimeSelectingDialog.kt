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

class WeatherTimeSelectingDialog(
    context: Context,
    private val listTimes: List<TimeSelect>,
    private val backTimeList: (list: List<TimeSelect>) -> Unit
) : Dialog(context) {
    private val rvTimesLayoutManager = LinearLayoutManager(context)
    private var acceptedList = listOf<TimeSelect>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_weather_time_selecting)
        initUi()
    }

    private fun initUi() {
        btnAccept.setOnClickListener {
            val allSelected = acceptedList.filter { it.selected }
            if(allSelected.isNullOrEmpty())
                return@setOnClickListener
            backTimeList(acceptedList)
            dismiss()
        }

        btnCancel.setOnClickListener { dismiss() }

        rvTimes.apply {
            layoutManager = rvTimesLayoutManager
            adapter = TimeSelectingAdapter(listTimes) {
                acceptedList = it
            }
        }
    }
}