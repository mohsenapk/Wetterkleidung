package com.mohsen.apk.wetterkleidung.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDagger()
        initViewModel()
        initUi()
        viewModel.start()
        viewModelListener()
    }

    private fun viewModelListener() {
        viewModel.gotoCityFragment.observe(this , Observer { loadFragment(CityFragment()) })
        viewModel.gotoWeatherFragment.observe(this , Observer { loadFragment(WeatherFragment()) })
        viewModel.changeLoaderImageResource.observe(this, Observer { imgLoading.setImageResource(it) })
    }

    private fun initUi() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun loadFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, fragment)
            .commit()
    }
}