package com.mohsen.apk.wetterkleidung.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseActivity
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherFragment
import javax.inject.Inject

class MainActivity() : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDagger()
        initViewModel()
        initUi()
        viewModelListener()
    }

    private fun initUi() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun viewModelListener() {
        liveDataListener(viewModel.gotoCityFragment) { gotoCityFragment() }
        liveDataListener(viewModel.gotoWeatherFragment) { replaceFragment(WeatherFragment.getInstance()) }
    }

    private fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .commit()
    }

    fun gotoCityFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, CityFragment.getInstance())
            .addToBackStack(CityFragment.javaClass.simpleName)
            .commit()
    }

}