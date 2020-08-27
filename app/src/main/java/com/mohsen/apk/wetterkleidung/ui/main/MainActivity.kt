package com.mohsen.apk.wetterkleidung.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.base.BaseFragment
import com.mohsen.apk.wetterkleidung.ui.city.CityFragment
import com.mohsen.apk.wetterkleidung.ui.setting.SettingFragment
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDagger()
        initViewModel()
        initUi()
        viewModel.start()
        viewModelListener()
    }

    private fun viewModelListener() {
        liveDataListener(viewModel.gotoCityFragment) { loadFragment(CityFragment.getInstance()) }
        liveDataListener(viewModel.gotoWeatherFragment) { loadFragment(WeatherFragment.getInstance()) }
        liveDataListener(viewModel.changeLoaderImageResource) { imgLoading.setImageResource(it) }
        liveDataListener(viewModel.finishApp) { finishAffinity() }
        liveDataListener(viewModel.showLoadingView) { loadingViewShowing(it) }
        liveDataListener(viewModel.removeTopBackStackItem) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private fun loadingViewShowing(it: Boolean) {
        if (it)
            loadingFrame.visibility = View.VISIBLE
        else
            loadingFrame.visibility = View.INVISIBLE
    }

    private fun initUi() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun loadFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

    private fun <T> liveDataListener(liveDataFunc: LiveData<T>, func: (obj: T) -> Unit) {
        liveDataFunc.observe(this, Observer {
            func(it)
        })
    }

    fun gotoFragment(fragmentJavaClassName: String) {
        when (fragmentJavaClassName) {
            CityFragment::class.java.name -> loadFragment(CityFragment.getInstance())
            SettingFragment::class.java.name -> loadFragment(SettingFragment.getInstance())
        }
    }

    fun backPressedFromFragment(fragmentJavaClassName: String) {
        when (fragmentJavaClassName) {
            WeatherFragment::class.java.name -> viewModel.backPressedFromWeatherFragment()
            CityFragment::class.java.name -> viewModel.backPressedFromCityFragment()
        }
    }

}