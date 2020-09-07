package com.mohsen.apk.wetterkleidung.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.ui.main.MainActivity


abstract class BaseFragment(private val layout: Int) : Fragment() {

    protected lateinit var application: BaseApplication
    protected lateinit var act: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = context as Activity
        application = (act.application as BaseApplication)
    }

    fun onBackPressed() {
        (act as MainActivity).backPressedFromFragment(this::class.java.name)
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) { view = View(activity) }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    abstract fun initDagger()
    abstract fun showSnackBarError(message: String)

    protected fun <T> liveDataListener(liveDataFunc: LiveData<T>, func: (obj: T) -> Unit) {
        liveDataFunc.observe(this, Observer {
            func(it)
        })
    }

    protected fun gotoFragment(strName: String) {
        (act as MainActivity).gotoFragment(strName)
    }

}