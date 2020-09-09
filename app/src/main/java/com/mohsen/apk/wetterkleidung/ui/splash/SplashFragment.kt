package com.mohsen.apk.wetterkleidung.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {
    companion object {
        fun getInstance(): SplashFragment = SplashFragment()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.backTop)
    }

    override fun initDagger() {}
    override fun showSnackBarError(message: String) {}
}