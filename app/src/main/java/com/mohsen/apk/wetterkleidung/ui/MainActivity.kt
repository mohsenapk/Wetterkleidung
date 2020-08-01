package com.mohsen.apk.wetterkleidung.ui

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.adapter.WeatherLowInfoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    private val linearLayoutManager = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDagger()
        initViewModel()
        viewModel.start()
        initUI()

    }

    private fun initUI() {
        seekBarSetup()
        viewModel.snackBarError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.seekBarMaxSize.observe(this, Observer {
            seekBar.max = it - 1
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
        viewModel.seekBarChangeProgress.observe(this, Observer { seekBar.progress = it })
        viewModel.seekBarTimeShow.observe(this, Observer { tvSeekTime.text = it })
        viewModel.weatherImageIconId.observe(this, Observer {
            it?.let { viewModel.weatherImageIconWithId(ivIcon, it) }
        })
        viewModel.weatherLowInfoList.observe(this, Observer {
            rvOtherWeather.apply {
                layoutManager = linearLayoutManager
                it?.let {
                    adapter = WeatherLowInfoAdapter(it)
                }
            }
        })
    }

    private fun injectDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun seekBarSetup() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.seekBarProgressChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

}
