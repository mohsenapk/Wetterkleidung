package com.mohsen.apk.wetterkleidung.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory

    lateinit var viewModel: SplashViewModel

    companion object {
        fun getInstance(): SplashFragment = SplashFragment()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.backTop)
        initDagger()
        initViewModel()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        liveDataListener(viewModel.showCityDefaultName) {
            clDefaultCity.visibility = View.VISIBLE
            tvCityName.text = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun initDagger() {
        (application as BaseApplication).splaComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {}
}