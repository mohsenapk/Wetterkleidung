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
    private val selectedList: List<TimeSelect>,
    private val backTimeList: (list: List<TimeSelect>) -> Unit
) : Dialog(context) {
    private val rvTimesLayoutManager = LinearLayoutManager(context)
    private var listTimes = mutableListOf(TimeSelect("select all", true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_weather_time_selecting)
        createTimeList()
        initUi()
    }

    private fun createTimeList() {
        SeekBarValue.values().toList().forEach {
            listTimes.add(TimeSelect(it.hours, false))
        }
        if (selectedList.isNotEmpty())
            changeListFromSelectedList()
        else
            listTimes.map { it.selected = true }
    }

    private fun changeListFromSelectedList() {
        listTimes.map { it.selected = false }
        listTimes.forEach { time ->
            selectedList.forEach { selectedTime ->
                if (time.text == selectedTime.text)
                    time.selected = true
            }
        }
    }

    private fun initUi() {
        btnAccept.setOnClickListener {
            if (listTimes.isEmpty())
                return@setOnClickListener
            backTimeList(listTimes)
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
}