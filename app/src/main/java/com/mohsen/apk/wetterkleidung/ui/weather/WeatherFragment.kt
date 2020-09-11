package com.mohsen.apk.wetterkleidung.ui.weather

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initViewModel()
        initUI()
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
            override fun onStartTrackingTouch(seekBar: TickSeekBar?) {}

            override fun onSeeking(seekParams: SeekParams?) {
                seekParams?.let { viewModel.seekBarProgressChangeOnSeeking(it.progress) }
            }

            override fun onStopTrackingTouch(seekBar: TickSeekBar?) {
                seekBar?.let { viewModel.seekBarProgressChangedOnStopTouching(it.progress) }
            }
        }
    }

    private fun seekBarVirtualFirstItemClick(){
        seekBar?.let { viewModel.seekBarProgressChangedOnStopTouching(0) }
    }

    private fun seekBarInitTexts(seekTimes: List<String>) {
        seekBar.tickCount = seekTimes.size
        seekBar.max = seekTimes.size.toFloat() - 1
        seekBar.customTickTexts(seekTimes.toTypedArray())
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun listenToViewModel() {
        liveDataListener(viewModel.cityName) { tvCity.text = it }
        liveDataListener(viewModel.dayName) { tvDayName.text = it }
        liveDataListener(viewModel.weatherLowInfoList) { initRvOtherWeather(it) }
        liveDataListener(viewModel.seekBarSelectedText) { tvSeekTime.text = it }
        liveDataListener(viewModel.seekBarTextList) { seekBarInitTexts(it) }
        liveDataListener(viewModel.seekTimeProgress) { seekBar.setProgress(it) }
        liveDataListener(viewModel.tempDesc) { tvTempDesc.text = it }
        liveDataListener(viewModel.changeBackImage) { imgBack.setImageResource(it) }
        liveDataListener(viewModel.changeStatusBarColor) { setStatusBarColor(it) }
        liveDataListener(viewModel.changeTextColor) { changeAllTextColors(it) }
        liveDataListener(viewModel.changeAvatar) {
            imgAvatar.startAnimation(AnimationUtils.loadAnimation(act, android.R.anim.fade_out))
            imgAvatar.setImageResource(it)
            imgAvatar.startAnimation(AnimationUtils.loadAnimation(act, android.R.anim.fade_in))
        }
        liveDataListener(viewModel.progressAvatarImageVisible) {
            progressImgAvatar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        liveDataListener(viewModel.imgAvatarUmbrellaVisible) {
            imgAvatarUmbrella.visibility = if (it) View.VISIBLE else View.GONE
        }
        liveDataListener(viewModel.changeBackBottomColor) {
            clParent.setBackgroundColor(ContextCompat.getColor(act, it))
        }
        liveDataListener(viewModel.snackBarError) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        liveDataListener(viewModel.progressVisible) {
            progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        liveDataListener(viewModel.parentVisible) {
            clParent.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        liveDataListener(viewModel.weatherImageIconId) {
            it?.let { viewModel.weatherIconLoader(imgWeatherIcon, it) }
        }
        liveDataListener(viewModel.temp) {
            tvTemp.text = it.toString()
            tvTempDegreeIcon.visibility = View.VISIBLE
        }
    }

    private fun changeAllTextColors(color: Int) {
        tvDayName.setTextColor(ContextCompat.getColor(act, color))
        tvCity.setTextColor(ContextCompat.getColor(act, color))
        tvTemp.setTextColor(ContextCompat.getColor(act, color))
        tvTempDegreeIcon.setTextColor(ContextCompat.getColor(act, color))
        tvTempDesc.setTextColor(ContextCompat.getColor(act, color))
        tvSeekTime.setTextColor(ContextCompat.getColor(act, color))
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
