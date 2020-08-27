package com.mohsen.apk.wetterkleidung.ui.weather


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.WeatherLowInformation
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar
import kotlinx.android.synthetic.main.fragment_weather.*
import javax.inject.Inject

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {
    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var imageHelper: ImageHelper

    lateinit var viewModel: WeatherViewModel
    private val rvOtherWeatherLayoutManager = LinearLayoutManager(context)

    override fun initDagger() {
        ((context as Activity).application as BaseApplication).weatherComponent.inject(this)
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
        tvCity.setOnClickListener { gotoCityActivity() }
        imgSetting.setOnClickListener { gotoSettingActivity() }
        seekBarInit()
    }

    private fun gotoSettingActivity() {
//        startActivity(Intent(this, SettingFragment::class.java))
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
        viewModel.snackBarError.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.cityName.observe(this, Observer { tvCity.text = it })
        viewModel.dayName.observe(this, Observer { tvDayName.text = it })
        viewModel.temp.observe(this, Observer {
            tvTemp.text = it.toString()
            tvTempDegreeIcon.visibility = View.VISIBLE
        })
        viewModel.tempDesc.observe(this, Observer { tvTempDesc.text = it })
        viewModel.progress.observe(this, Observer {
            progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        viewModel.weatherImageIconId.observe(this, Observer {
            it?.let { viewModel.weatherIconLoader(ivIcon, it) }
        })
        viewModel.weatherLowInfoList.observe(this, Observer { initRvOtherWeather(it) })
        viewModel.goToCityActivity.observe(this, Observer { gotoCityActivity() })
        viewModel.seekBarSelectedText.observe(this, Observer { tvSeekTime.text = it })
        viewModel.seekBarTextList.observe(this, Observer { seekBarInitTexts(it) })
        viewModel.seekTimeProgress.observe(this, Observer { seekBar.setProgress(it) })
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

    private fun gotoCityActivity() {
//        startActivity(Intent(this, CityFragment::class.java))
    }

}
