package com.mohsen.apk.wetterkleidung.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDagger()
        initViewModel()
        initUI()
        viewModel.getWeathers()
    }

    private fun injectDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initUI() {
        viewModel.currentWeather.observe(this, Observer {
            tv.append("\n")
            tv.append("----- current -----")
            tv.append("\n")
            tv.append(it.toString())
        })
        viewModel.forecastWeather.observe(this , Observer {
            tv.append("\n")
            tv.append("----- forecast -----")
            tv.append("\n")
            tv.append(it.toString())
        })
    }
}
