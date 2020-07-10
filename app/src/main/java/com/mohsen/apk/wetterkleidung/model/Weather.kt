package com.mohsen.apk.wetterkleidung.model

import com.google.gson.annotations.SerializedName

data class WeatherTitle(
    val id: Int = 0,
    @SerializedName("main")
    val title: String = "",
    val description: String = "",
    val icon: String = ""
)

data class CityLocation(
    val lon: Double = 0.0,
    val lat: Double = 0.0
)