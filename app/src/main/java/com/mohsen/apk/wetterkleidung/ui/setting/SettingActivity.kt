package com.mohsen.apk.wetterkleidung.ui.setting

import android.content.Intent
import android.os.BaseBundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.city.CityActivity
import kotlinx.android.synthetic.main.activity_setting.*
import javax.inject.Inject

class SettingActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: SettingViewModelFactory
    lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initDagger()
        initViewModel()
        initUi()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        viewModel.exitApp.observe(this, Observer {
            finishAffinity() })
        viewModel.gotoCityActivity.observe(this, Observer { gotoCityActivity() })
        viewModel.gotoTimeSetting.observe(this , Observer {  })
    }

    private fun gotoCityActivity() {
        startActivity(Intent(this, CityActivity::class.java))
    }

    private fun initUi() {
        clCity.setOnClickListener { viewModel.citySettingClicked() }
        clTimes.setOnClickListener { viewModel.timeSettingClicked() }
        clExit.setOnClickListener { viewModel.exitAppClicked() }
        radioUnitC.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.weatherUnitCelsiusClicked()
        }
        radioUnitF.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.weatherUnitFahrenheitClicked()
        }
        switchAdvance.setOnCheckedChangeListener { _, isChecked ->
            viewModel.advancedAppClicked(isChecked)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).settingComponent.inject(this)
    }
}