package com.mohsen.apk.wetterkleidung.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.city.CityActivity
import com.mohsen.apk.wetterkleidung.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject


class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory
    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initDagger()
        initViewModel()
        viewModel.start()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        viewModel.gotoCityActivity.observe(this, Observer { gotoCityActivity() })
        viewModel.gotoMainActivity.observe(this, Observer { gotoMainActivity() })
        viewModel.changeLoaderImageResource.observe(this, Observer {
            imgLoading.setImageResource(it)
            imgLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alfa_0_1))
        })
    }

    private fun gotoCityActivity() {
        val intent = Intent(this, CityActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).splashComponent.inject(this)
    }
}