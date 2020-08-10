package com.mohsen.apk.wetterkleidung.utility

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mohsen.apk.wetterkleidung.BuildConfig

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
        Glide.with(imageView)
            .load("${BuildConfig.API_URL_ICON}$iconId@4x.png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}