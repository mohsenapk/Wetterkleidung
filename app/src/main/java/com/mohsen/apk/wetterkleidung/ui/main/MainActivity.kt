package com.mohsen.apk.wetterkleidung.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.WeatherLowInformation
import com.mohsen.apk.wetterkleidung.ui.adapter.SeekTimeAdapter
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import com.mohsen.apk.wetterkleidung.ui.city.CityActivity
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import kotlinx.android.synthetic.main.activity_main.*
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    @Inject
    lateinit var imageHelper: ImageHelper

    lateinit var viewModel: MainViewModel
    private val linearLayoutManagerVertical = LinearLayoutManager(this)
    private var rvSeekBarLastPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDagger()
        setContentView(R.layout.activity_main)
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
        LinearSnapHelper().attachToRecyclerView(rvSeekTimes)
        tvCity.setOnClickListener { gotoCityActivity() }
    }

    private fun listenToViewModel() {
        viewModel.snackBarError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
        viewModel.seekBarTimes.observe(this, Observer { initRvSeekTime(it) })
        viewModel.weatherLowInfoList.observe(this, Observer { initRvOtherWeather(it) })
        viewModel.humidity.observe(this, Observer { tvHu.text = "humidity: $it" })
        viewModel.wind.observe(this, Observer { tvWind.text = "wind + degree : $it" })
        viewModel.clouds.observe(this, Observer { tvCloudes.text = "clouds : $it" })
        viewModel.goToCityActivity.observe(this, Observer { gotoCityActivity() })
    }

    private fun initRvSeekTime(list: List<Int>) {
        rvSeekTimes.apply {
            layoutManager = getPickerLayoutManager()
            list?.let {
                adapter = SeekTimeAdapter(it)
            }
            rvSeekTimes.smoothScrollToPosition(0)
        }
    }

    private fun initRvOtherWeather(list: List<WeatherLowInformation>) {
        rvOtherWeather.apply {
            layoutManager = linearLayoutManagerVertical
            list?.let {
                adapter = WeatherLowInfoAdapter(list, imageHelper) {
                    viewModel.dateChanged(it)
                }
            }
        }
    }

    private fun getPickerLayoutManager(): RecyclerView.LayoutManager {
        val pickerLayoutManager = PickerLayoutManager(
            this, PickerLayoutManager.HORIZONTAL, false
        )
        pickerLayoutManager.apply {
            isChangeAlpha = true
            scaleDownBy = 1.5f
            scaleDownDistance = 1.5f
        }
        pickerLayoutManager.setOnScrollStopListener {
            val position = pickerLayoutManager.findLastVisibleItemPosition()
            if (position >= 0 && position != rvSeekBarLastPosition) {
                rvSeekBarLastPosition = position
                viewModel.rvSeekBarChangeIndex(position)
            }
        }
        return pickerLayoutManager
    }

    private fun injectDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun gotoCityActivity() {
        startActivity(Intent(this, CityActivity::class.java))
    }

}
