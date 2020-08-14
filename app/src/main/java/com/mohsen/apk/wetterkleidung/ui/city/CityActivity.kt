package com.mohsen.apk.wetterkleidung.ui.city

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.ui.adapter.CityAdapter
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import kotlinx.android.synthetic.main.activity_city.*
import javax.inject.Inject

class CityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: CityViewModelFactory

    @Inject
    lateinit var imageHelper: ImageHelper

    lateinit var viewModel: CityViewModel
    private val linearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        initDagger()
        initViewModel()
        viewModel.start()
        initUI()
        listenToViewModel()
    }

    private fun listenToViewModel() {
        viewModel.showAllCities.observe(this, Observer {
            initRvCities(it)
        })
    }

    private fun initRvCities(list: List<City>) {
        rvCities.apply {
            layoutManager = linearLayoutManager
            adapter = CityAdapter(list, imageHelper)
        }
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