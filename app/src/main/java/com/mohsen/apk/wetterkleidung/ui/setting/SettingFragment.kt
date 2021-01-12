package com.mohsen.apk.wetterkleidung.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.TimeSelect
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogManager
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

class SettingFragment : BaseFragment(R.layout.fragment_setting) {

    companion object {
        fun getInstance(): SettingFragment = SettingFragment()
    }

    @Inject
    lateinit var viewModelFactory: SettingViewModelFactory

    @Inject
    lateinit var dialogManager: DialogManager

    lateinit var viewModel: SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        listenToViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun listenToViewModel() {
        liveDataListener(viewModel.gotoCityFragment) {
            gotoFragment(CityFragment.getInstance(), addToBackStack = true)
        }
        liveDataListener(viewModel.exitApp) { activity?.let { it.finishAffinity() } }
        liveDataListener(viewModel.showSnackBarText) { showSnackBarText(it) }
        liveDataListener(viewModel.showTimeSelectingDialog) {
            showTimeSelectingDialog(it)
        }
        liveDataListener(viewModel.setWeatherUnitRadioButtonToImperial) {
            radioGroup.check(R.id.radioUnitF)
        }
        liveDataListener(viewModel.setWeatherUnitRadioButtonToMetric) {
            radioGroup.check(R.id.radioUnitC)
        }
        liveDataListener(viewModel.setAdvanceApp){
            switchAdvance.isChecked = it
        }
    }

    private fun showTimeSelectingDialog(list: List<TimeSelect>) {
        dialogManager.showWeatherTimeSelectingDialog(act, list) {
            viewModel.changeTimeSelectedList(it)
        }
    }

    private fun showSnackBarText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun initUI() {
        clCity.setOnClickListener { viewModel.cityClClicked() }
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

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingViewModel::class.java)
    }

    override fun initDagger() {
        application.settingComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {
        TODO("Not yet implemented")
    }
}