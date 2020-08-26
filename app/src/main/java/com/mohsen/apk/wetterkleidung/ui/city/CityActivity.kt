package com.mohsen.apk.wetterkleidung.ui.city

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.ui.adapter.CityAdapter
import com.mohsen.apk.wetterkleidung.ui.main.MainActivity
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
        viewModel.showAllCities.observe(this, Observer { initRvCities(it) })
        viewModel.showSnackBarError.observe(this, Observer { showSnackBarError(it) })
        viewModel.goToMainActivity.observe(this, Observer { gotoMainActivity() })
        viewModel.goToLastActivity.observe(this, Observer { backToLastActivity() })
        viewModel.showNoneCitySelectedError.observe(this, Observer {
            clNoneCity.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        viewModel.finishApp.observe(this, Observer { finishApp() })
    }

    private fun finishApp() {
        finishAffinity()
    }

    private fun initRvCities(list: List<City>) {
        rvCities.apply {
            layoutManager = linearLayoutManager
            adapter = CityAdapter(list, imageHelper) {
                viewModel.rvCityClicked(it)
            }
        }
    }

    private fun initUI() {
        etCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                imgAdd.callOnClick()
            return@setOnEditorActionListener true
        }
        imgAdd.setOnClickListener { viewModel.addCityClicked(etCity.text.toString()) }
        fabGPS.setOnClickListener { viewModel.fabGpsClicked() }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).cityComponent.inject(this)
    }

    private fun showSnackBarError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    private fun backToLastActivity() {
        super.onBackPressed()
    }

    private fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}