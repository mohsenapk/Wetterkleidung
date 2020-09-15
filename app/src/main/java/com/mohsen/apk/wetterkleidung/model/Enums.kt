package com.mohsen.apk.wetterkleidung.model

enum class WeatherUnit {
    METRIC,
    IMPERIAL
}

enum class SeekBarValue(
    val hour: Int,
    val hours: String,
    val minText: String
) {
    ZERO(0, "00:00-03:00", "00"),
    ONE(1, "03:00-06:00", "03"),
    TWO(2, "06:00-09:00", "06"),
    THREE(3, "09:00-12:00", "09"),
    FOUR(4, "12:00-15:00", "12"),
    FIVE(5, "15:00-18:00", "15"),
    SIX(6, "18:00-21:00", "18"),
    SEVEN(7, "21:00-24:00", "21"),
}