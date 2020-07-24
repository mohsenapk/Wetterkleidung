package com.mohsen.apk.wetterkleidung.model

enum class WeatherUnit {
    METRIC
}

enum class SeekBarValue(
    val hour: Int,
    val hours: String
) {
    ZERO(0, "6:00 - 9:00. am"),
    ONE(1, "9:00 - 12:00. am"),
    TWO(2, "12:00 - 15:00. pm"),
    THREE(3, "15:00 - 18:00. pm"),
    FOUR(4, "18:00 - 21:00. pm"),
    FIVE(5, "21:00 - 24:00. pm"),
    SIX(6, "24:00 - 03:00. am"),
    SEVEN(7, "03:00 - 06:00. am"),
}