package com.mohsen.apk.wetterkleidung.ui.splash

import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    companion object {
        fun getInstance(): SplashFragment = SplashFragment()
    }

    override fun initDagger() {}
    override fun showSnackBarError(message: String) {}
}