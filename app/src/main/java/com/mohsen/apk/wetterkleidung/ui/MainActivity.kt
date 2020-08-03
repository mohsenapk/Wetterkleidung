package com.mohsen.apk.wetterkleidung.ui

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
import com.mohsen.apk.wetterkleidung.ui.adapter.SeekTimeAdapter
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    private val linearLayoutManagerVertical = LinearLayoutManager(this)
    private var rvSeekBarLastPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDagger()
        initViewModel()
        initUI()
        viewModel.start()
        listenToViewModel()
    }

    private fun initUI() {
        LinearSnapHelper().attachToRecyclerView(rvSeekTimes)
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
        viewModel.seekBarChangeProgress.observe(this, Observer {
            //rvSeekBarClick
        })
        viewModel.weatherImageIconId.observe(this, Observer {
            it?.let { viewModel.weatherImageIconWithId(ivIcon, it) }
        })
        viewModel.seekBarTimes.observe(this, Observer {
            rvSeekTimes.apply {
                layoutManager = getPickerLayoutManager()
                it?.let {
                    adapter = SeekTimeAdapter(it)
                }
                rvSeekTimes.smoothScrollToPosition(0)
            }
        })
        viewModel.weatherLowInfoList.observe(this, Observer {
            rvOtherWeather.apply {
                layoutManager = linearLayoutManagerVertical
                it?.let {
                    adapter = WeatherLowInfoAdapter(it)
                }
            }
        })
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
            if (position >= 0 && position != rvSeekBarLastPosition)
                rvSeekBarLastPosition = position
            viewModel.rvSeekBarChangeIndex(rvSeekBarLastPosition)
        }
        return pickerLayoutManager
    }

    private fun injectDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

}
