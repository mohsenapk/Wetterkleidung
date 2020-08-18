package com.mohsen.apk.wetterkleidung.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.city.CityActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initDagger()
        initViewModel()
        viewModel.start()
        listenToViewModel()
    }

    private fun initDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    fun listenToViewModel() {
        viewModel.goToCityActivity.observe(this, Observer { goToCityActivity() })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun goToCityActivity() {
        startActivity(Intent(this, CityActivity::class.java))
    }
}