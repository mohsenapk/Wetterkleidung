package com.mohsen.apk.wetterkleidung.ui.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.base.BaseApplication


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
        initDagger()
        initViewModel()
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setVisibility(it: Boolean) = if (it) View.VISIBLE else View.GONE

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(colorId: Int) {
        val window: Window = act.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(act, colorId)
    }

    abstract fun initDagger()
    abstract fun initViewModel()
    abstract fun showSnackBarError(message: String)

    fun <T> liveDataListener(liveDataFunc: LiveData<T>, func: (obj: T) -> Unit) {
        liveDataFunc.observe(this, Observer {
            func(it)
        })
    }

    fun gotoFragment(fragment: BaseFragment, addToBackStack: Boolean = false) {
        fragmentManager?.let {
            val transaction = it.beginTransaction()
                .add(R.id.mainFrame, fragment)
            if (addToBackStack) transaction.addToBackStack(fragment::class.java.simpleName)
            transaction.commit()
        }
    }
}