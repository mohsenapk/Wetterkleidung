package com.mohsen.apk.wetterkleidung.ui

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.SeekBarValue
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    lateinit var viewModel: MainViewModel

    private var lastSeekBarValue: SeekBarValue = SeekBarValue.ONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDagger()
        initViewModel()
        viewModel.refreshWeather(imageView = ivIcon)
        initUI()
        seekBarSetup()
        seekBarSetupFirstTime()
    }

    private fun initUI() {
        viewModel.snackBarError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.cityName.observe(this, Observer { tvCity.text = it })
        viewModel.date.observe(this, Observer { tvDate.text = it })
        viewModel.dayName.observe(this, Observer { tvDayName.text = it })
        viewModel.temp.observe(this, Observer {
            tvTemp.text = it.toString()
            tvTempDegreeIcon.visibility = View.VISIBLE
        })
        viewModel.tempDesc.observe(this, Observer { tvTempDesc.text = it })
        viewModel.progress.observe(this, Observer {
            progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
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
                when (progress) {
                    0 -> lastSeekBarValue = SeekBarValue.ZERO
                    1 -> lastSeekBarValue = SeekBarValue.ONE
                    2 -> lastSeekBarValue = SeekBarValue.TWO
                    3 -> lastSeekBarValue = SeekBarValue.THREE
                    4 -> lastSeekBarValue = SeekBarValue.FOUR
                    5 -> lastSeekBarValue = SeekBarValue.FIVE
                    6 -> lastSeekBarValue = SeekBarValue.SIX
                    7 -> lastSeekBarValue = SeekBarValue.SEVEN
                }
                tvSeekTime.text = lastSeekBarValue.hours
                Timber.d("seek - onProgressChange - > $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun seekBarSetupFirstTime() {
        when (LocalDateTime.now().hour) {
            6, 7, 8 -> seekBar.progress = 0
            9, 10, 11 -> seekBar.progress = 1
            12, 13, 14 -> seekBar.progress = 2
            15, 16, 17 -> seekBar.progress = 3
            18, 19, 20 -> seekBar.progress = 4
            21, 22, 23 -> seekBar.progress = 5
            0, 1, 2 -> seekBar.progress = 6
            3, 4, 5 -> seekBar.progress = 7
        }
    }
}
