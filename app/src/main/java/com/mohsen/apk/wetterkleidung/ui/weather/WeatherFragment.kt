package com.mohsen.apk.wetterkleidung.ui.weather


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.WeatherLowInformation
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.setting.SettingFragment
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar
import kotlinx.android.synthetic.main.fragment_weather.*
import javax.inject.Inject

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    companion object {
        fun getInstance(): WeatherFragment = WeatherFragment()
    }

    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var imageHelper: ImageHelper

    lateinit var viewModel: WeatherViewModel
    private val rvOtherWeatherLayoutManager = LinearLayoutManager(context)

    override fun initDagger() {
        application.weatherComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initViewModel()
        initUI()
        viewModel.start()
        listenToViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun initUI() {
        tvCity.setOnClickListener { gotoFragment(CityFragment::class.java.name) }
        imgSetting.setOnClickListener { gotoFragment(SettingFragment::class.java.name) }
        seekBarInit()
    }

    private fun seekBarInit() {
        seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                seekParams?.let { viewModel.seekBarProgressChanged(it.progress) }
            }

            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: TickSeekBar?) {}
        }
    }

    private fun seekBarInitTexts(seekTimes: List<String>) {
        seekBar.tickCount = seekTimes.size
        seekBar.max = seekTimes.size.toFloat() - 1
        seekBar.customTickTexts(seekTimes.toTypedArray())
    }

    private fun listenToViewModel() {
        liveDataListener(viewModel.cityName) { tvCity.text = it }
        liveDataListener(viewModel.dayName) { tvDayName.text = it }
        liveDataListener(viewModel.weatherLowInfoList) { initRvOtherWeather(it) }
        liveDataListener(viewModel.seekBarSelectedText) { tvSeekTime.text = it }
        liveDataListener(viewModel.seekBarTextList) { seekBarInitTexts(it) }
        liveDataListener(viewModel.seekTimeProgress) { seekBar.setProgress(it) }
        liveDataListener(viewModel.tempDesc) { tvTempDesc.text = it }
        liveDataListener(viewModel.snackBarError) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        liveDataListener(viewModel.progress) {
            progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        liveDataListener(viewModel.weatherImageIconId) {
            it?.let { viewModel.weatherIconLoader(ivIcon, it) }
        }
        liveDataListener(viewModel.temp) {
            tvTemp.text = it.toString()
            tvTempDegreeIcon.visibility = View.VISIBLE
        }
    }


    private fun initRvOtherWeather(list: List<WeatherLowInformation>) {
        rvOtherWeather.apply {
            layoutManager = rvOtherWeatherLayoutManager
            list?.let {
                adapter = WeatherLowInfoAdapter(list, imageHelper) {
                    viewModel.dateChanged(it)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
    }

}
