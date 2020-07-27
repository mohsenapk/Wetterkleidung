package com.mohsen.apk.wetterkleidung.utility

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

interface ImageHelper {
    fun loadWeatherIcon(
        imageView: ImageView,
        iconId: String
    )
}

const val API_URL_ICON = "http://openweathermap.org/img/wn/"

class ImageHelperImpl : ImageHelper {
    override fun loadWeatherIcon(
        imageView: ImageView,
        iconId: String
    ) {
        Glide.with(imageView)
            .load("$API_URL_ICON$iconId@4x.png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(imageView)
    }
}