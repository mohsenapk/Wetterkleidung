package com.mohsen.apk.wetterkleidung.ui.main

import android.os.Bundle
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
import com.mohsen.apk.wetterkleidung.ui.splash.SplashFragment
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherFragment
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
        viewModel.start()
        initUi()
        viewModelListener()
    }

    private fun viewModelListener() {
        liveDataListener(viewModel.gotoSplashFragment) { replaceFragment(SplashFragment.getInstance()) }
        liveDataListener(viewModel.gotoCityFragment) { addFragment(CityFragment.getInstance()) }
        liveDataListener(viewModel.gotoWeatherFragment) { addFragment(WeatherFragment.getInstance()) }
        liveDataListener(viewModel.changeLoaderImageResource) { imgLoading.setImageResource(it) }
        liveDataListener(viewModel.finishApp) { finishAffinity() }
        liveDataListener(viewModel.callOnBackPressed) { onBackPressedCustom() }
        liveDataListener(viewModel.hasWeatherFragmentINnStackOrNot) {
            it.hasFragment(hasWeatherFragmentInStock())
        }
        liveDataListener(viewModel.removeTopBackStackItem) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private fun hasWeatherFragmentInStock(): Boolean {
        val weatherFragment =
            supportFragmentManager
                .fragments
                .firstOrNull { it.tag == WeatherFragment::class.java.name }
        return weatherFragment != null
    }


    private fun initUi() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initDagger() {
        (application as BaseApplication).mainComponent.inject(this)
    }

    private fun addFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

    private fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .commit()
    }

    private fun <T> liveDataListener(liveDataFunc: LiveData<T>, func: (obj: T) -> Unit) {
        liveDataFunc.observe(this, Observer {
            func(it)
        })
    }

    fun gotoFragment(fragmentJavaClassName: String) {
        when (fragmentJavaClassName) {
            CityFragment::class.java.name -> addFragment(CityFragment.getInstance())
            SettingFragment::class.java.name -> addFragment(SettingFragment.getInstance())
        }
    }

    fun backPressedFromFragment(fragmentJavaName: String) {
        when (fragmentJavaName) {
            CityFragment::class.java.name -> viewModel.backedFromCityFragment()
            SettingFragment::class.java.name -> viewModel.backFromSettingFragment()
            else -> onBackPressedCustom()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            (it as BaseFragment).onBackPressed()
        }
    }

    private fun onBackPressedCustom() {
        super.onBackPressed()
    }
}