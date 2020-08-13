package com.mohsen.apk.wetterkleidung.ui.city

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogAddCity
import com.mohsen.apk.wetterkleidung.ui.dialog.DialogShowingManager
import kotlinx.android.synthetic.main.activity_city.*
import javax.inject.Inject

class CityActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: CityViewModelFactory
    @Inject
    lateinit var dialogShowingManager: DialogShowingManager

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
        viewModel.showAddCityDialog.observe(this, Observer {
            dialogShowingManager.showAddCityDialog(this)
        })
    }

    private fun initUI() {
        clAddCity.setOnClickListener { viewModel.addCityClicked() }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CityViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).cityComponent.inject(this)
    }
}