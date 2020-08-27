package com.mohsen.apk.wetterkleidung.ui.city

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.ui.adapter.CityAdapter
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import kotlinx.android.synthetic.main.fragment_city.*
import timber.log.Timber
import javax.inject.Inject

private const val locationRequestCode = 10000

class CityFragment : BaseFragment(R.layout.fragment_city) {

    @Inject
    lateinit var viewModelFactory: CityViewModelFactory

    @Inject
    lateinit var imageHelper: ImageHelper
    lateinit var viewModel: CityViewModel
    private val linearLayoutManager = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.goToLastActivity.observe(this, Observer {

        })
        viewModel.showNoneCitySelectedError.observe(this, Observer {
            clNoneCity.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        viewModel.finishApp.observe(this, Observer { finishApp() })
        viewModel.getLocationPermission.observe(this, Observer { getLocationPermission() })
    }

    private fun getLocationPermission() {
            if (ContextCompat.checkSelfPermission(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationRequestCode
                )
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Timber.d("location -permission granted")
                fabGPS.callOnClick()
            }
        }
    }

    private fun finishApp() {
//        finishAffinity()
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

    override fun initDagger() {
        ((context as Activity).application as BaseApplication).cityComponent.inject(this)
    }

    override fun showSnackBarError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun gotoMainActivity() {
//        val intent = Intent(this, WeatherFragment::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
    }
}