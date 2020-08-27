package com.mohsen.apk.wetterkleidung.ui.setting

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    @Inject
    lateinit var viewModelFactory: SettingViewModelFactory
    lateinit var viewModel: SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initViewModel()
        initUi()
        listenToViewModel()
    }



    private fun listenToViewModel() {
        viewModel.exitApp.observe(this, Observer {

        })
        viewModel.gotoCityActivity.observe(this, Observer { gotoCityActivity() })
        viewModel.gotoTimeSetting.observe(this , Observer {  })
    }

    private fun gotoCityActivity() {
//        startActivity(Intent(this, CityFragment::class.java))
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

    override fun initDagger() {
        ((context as Activity).application as BaseApplication).settingComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {
        TODO("Not yet implemented")
    }
}