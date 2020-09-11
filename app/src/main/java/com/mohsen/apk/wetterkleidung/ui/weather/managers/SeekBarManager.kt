package com.mohsen.apk.wetterkleidung.ui.weather.managers

import com.mohsen.apk.wetterkleidung.model.SeekBarValue

interface SeekBarManager {
    fun getSeekBarValues(maxSize: Int): List<Int>
    fun getTinySeekBarTextFromIndex(index: Int): String
    fun getSeekBarTextFromIndex(index: Int): String
}

class SeekBarManagerImpl : SeekBarManager {

    override fun getSeekBarValues(maxSize: Int): List<Int> {
        return listOf<Int>(7, 6, 5, 4, 3, 2, 1, 0).subList(0, maxSize).reversed()
    }

    override fun getTinySeekBarTextFromIndex(index: Int): String {
        return when (index) {
            0 -> SeekBarValue.ZERO.minText
            1 -> SeekBarValue.ONE.minText
            2 -> SeekBarValue.TWO.minText
            3 -> SeekBarValue.THREE.minText
            4 -> SeekBarValue.FOUR.minText
            5 -> SeekBarValue.FIVE.minText
            6 -> SeekBarValue.SIX.minText
            7 -> SeekBarValue.SEVEN.minText
            else -> ""
        }
    }

    override fun getSeekBarTextFromIndex(index: Int): String {
        return when (index) {
            0 -> SeekBarValue.ZERO.hours
            1 -> SeekBarValue.ONE.hours
            2 -> SeekBarValue.TWO.hours
            3 -> SeekBarValue.THREE.hours
            4 -> SeekBarValue.FOUR.hours
            5 -> SeekBarValue.FIVE.hours
            6 -> SeekBarValue.SIX.hours
            7 -> SeekBarValue.SEVEN.hours
            else -> ""
        }
    }

}