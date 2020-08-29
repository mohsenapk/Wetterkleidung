package com.mohsen.apk.wetterkleidung.ui.setting

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    companion object {
        fun getInstance(): SettingFragment = SettingFragment()
    }

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
        liveDataListener(viewModel.exitApp) { activity?.let { it.finishAffinity() } }
        liveDataListener(viewModel.showSnackBarText){showSnackBarText(it)}
    }

    private fun showSnackBarText(text: String) {
        Toast.makeText(context ,text , Toast.LENGTH_SHORT).show()
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
        application.settingComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {
        TODO("Not yet implemented")
    }
}