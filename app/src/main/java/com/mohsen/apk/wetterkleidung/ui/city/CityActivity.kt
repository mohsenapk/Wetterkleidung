package com.mohsen.apk.wetterkleidung.ui.city

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_city.*
import javax.inject.Inject

class CityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: CityViewModelFactory

    lateinit var viewModel: CityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        initDagger()
        initViewModel()
        initUI()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        viewModel.showAllCities.observe(this, Observer {
            var t = ""
            it.forEach { str ->
                t += "\n $str"
            }
            Toast.makeText(this, t, Toast.LENGTH_LONG).show()
        })
    }

    private fun initUI() {
        imgAdd.setOnClickListener { viewModel.addCityClicked(etCity.text.toString()) }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).cityComponent.inject(this)
    }
}