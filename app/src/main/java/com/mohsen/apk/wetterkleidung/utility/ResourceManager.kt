package com.mohsen.apk.wetterkleidung.utility

import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeatherDetail
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import kotlin.math.roundToInt

interface ResourceManager {
    fun getAvatarResourceIdFromWeather(
        weather: Forecast5DaysWeatherDetail,
        unit: WeatherUnit
    ): Int

    fun getTextColorId(index: Int): Int
    fun getBackImageResourceId(index: Int): Int
    fun getStatusColorId(index: Int): Int
    fun getBackBottomColorId(index: Int): Int
}

class ResourceManagerImpl : ResourceManager {

    override fun getAvatarResourceIdFromWeather(
        weather: Forecast5DaysWeatherDetail,
        unit: WeatherUnit
    ): Int {
        return if (unit == WeatherUnit.METRIC)
            getAvatarWithWeatherMetric(weather)
        else
            getAvatarWithWeatherImperial(weather)
    }

    private fun getAvatarWithWeatherImperial(weather: Forecast5DaysWeatherDetail): Int {
        return when (weather?.temp?.feels_like?.roundToInt()) {
            in (-148..41) -> R.drawable.avatar_cold_very
            in (41..50) -> R.drawable.avatar_cold
            in (50..59) -> R.drawable.avatar_cold_little
            in (59..68) -> R.drawable.avatar_normal
            in (68..77) -> R.drawable.avatar_hot_little
            in (77..86) -> R.drawable.avatar_hot
            in (86..212) -> R.drawable.avatar_hot_very
            else -> 0
        }
    }

    private fun getAvatarWithWeatherMetric(weather: Forecast5DaysWeatherDetail): Int {
        return when (weather?.temp?.feels_like?.roundToInt()) {
            in (-100..5) -> R.drawable.avatar_cold_very
            in (5..10) -> R.drawable.avatar_cold
            in (10..15) -> R.drawable.avatar_cold_little
            in (15..20) -> R.drawable.avatar_normal
            in (20..25) -> R.drawable.avatar_hot_little
            in (25..30) -> R.drawable.avatar_hot
            in (30..100) -> R.drawable.avatar_hot_very
            else -> 0
        }
    }

    override fun getTextColorId(index: Int): Int {
        return when (index) {
            6, 7 -> R.color.white
            else -> R.color.black
        }
    }

    override fun getBackImageResourceId(index: Int): Int {
        return when (index) {
            0, 1 -> R.drawable.back_day_break
            6 -> R.drawable.back_evening
            7 -> R.drawable.back_night
            else -> R.drawable.back_day
        }
    }

    override fun getStatusColorId(index: Int): Int {
        return when (index) {
            0, 1 -> R.color.backTopDayBreak
            6 -> R.color.backTopEvening
            7 -> R.color.backTopNight
            else -> R.color.backTopDay
        }
    }

    override fun getBackBottomColorId(index: Int): Int {
        return when (index) {
            0, 1 -> R.color.backBottomDayBreak
            6 -> R.color.backBottomEvening
            7 -> R.color.backBottomNight
            else -> R.color.backBottomDay
        }
    }

}