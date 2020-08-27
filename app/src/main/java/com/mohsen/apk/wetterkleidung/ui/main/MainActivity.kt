package com.mohsen.apk.wetterkleidung.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(CityFragment())
    }

    private fun loadFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, fragment)
            .commit()
    }
}