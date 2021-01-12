package com.mohsen.apk.wetterkleidung.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseActivity
import com.mohsen.apk.wetterkleidung.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory

    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initDagger()
        initViewModel()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        liveDataListener(viewModel.setLoadingImageResourceIs) { imgLoading.setImageResource(it) }
        liveDataListener(viewModel.gotoMainActivity) { gotoMainActivity() }
        liveDataListener(viewModel.showDefaultCityLayout) {
            clDefaultCity.visibility = View.VISIBLE
            tvCityName.text = it
        }
    }

    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun initDagger() {
        (application as BaseApplication).splashComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

}