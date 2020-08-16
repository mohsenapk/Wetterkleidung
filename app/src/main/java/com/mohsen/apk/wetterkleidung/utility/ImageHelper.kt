package com.mohsen.apk.wetterkleidung.utility

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.R

interface ImageHelper {
    fun loadWeatherIcon(
        imageView: ImageView,
        iconId: String
    )
}

class ImageHelperImpl : ImageHelper {

    override fun loadWeatherIcon(
        imageView: ImageView,
        iconId: String
    ) {
        if (!imageLoaderLocal(iconId, imageView))
            imageLoaderRemote(iconId, imageView)
    }

    private fun imageLoaderRemote(iconId: String, imageView: ImageView) {
        Glide.with(imageView)
            .load("${BuildConfig.API_URL_ICON}$iconId@4x.png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    private fun imageLoaderLocal(iconId: String, imageView: ImageView): Boolean {
        val icon = getImageResource(iconId)
        return if (icon > -1) {
            imageView.setImageResource(getImageResource(iconId))
            true
        } else
            false
    }

    private fun getImageResource(iconId: String): Int {
        return when (iconId) {
            "01d" -> R.drawable.weather_icon_01d
            "01n" -> R.drawable.weather_icon_01n
            "02d" -> R.drawable.weather_icon_02d
            "02n" -> R.drawable.weather_icon_02n
            "03d" -> R.drawable.weather_icon_03d
            "03n" -> R.drawable.weather_icon_03n
            "04d" -> R.drawable.weather_icon_04d
            "04n" -> R.drawable.weather_icon_04n
            "09d" -> R.drawable.weather_icon_09d
            "09n" -> R.drawable.weather_icon_09n
            "10d" -> R.drawable.weather_icon_10d
            "10n" -> R.drawable.weather_icon_10n
            "11d" -> R.drawable.weather_icon_11d
            "11n" -> R.drawable.weather_icon_11n
            "13d" -> R.drawable.weather_icon_13d
            "13n" -> R.drawable.weather_icon_13n
            "50d" -> R.drawable.weather_icon_50d
            "50n" -> R.drawable.weather_icon_50n
            else -> -1
        }
    }
}