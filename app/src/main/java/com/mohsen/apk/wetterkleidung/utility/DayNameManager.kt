package com.mohsen.apk.wetterkleidung.utility

import org.threeten.bp.LocalDateTime

interface DayNameManager {
    fun getDayName(timeStampNumber: Long): String
    fun getDayName(date: LocalDateTime): String
}

class DayNameManagerImpl(private val dateHelper: DateHelper) : DayNameManager {
    override fun getDayName(timeStampNumber: Long): String {
        return when {
            dateHelper.isToday(timeStampNumber) -> "Today"
            dateHelper.isMorning(timeStampNumber) -> "Tomorrow"
            else -> dateHelper.getDayOfWeekFromTimestamp(timeStampNumber)
                .toString().toLowerCase().capitalize()
        }
    }

    override fun getDayName(date: LocalDateTime): String {
        return when {
            dateHelper.isToday(date) -> "Today"
            dateHelper.isMorning(date) -> "Tomorrow"
            else -> dateHelper.getDayOfWeekFromTimestamp(date)
                .toString().toLowerCase().capitalize()
        }
    }
}