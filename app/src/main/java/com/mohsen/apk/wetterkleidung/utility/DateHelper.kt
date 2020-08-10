package com.mohsen.apk.wetterkleidung.utility

import com.mohsen.apk.wetterkleidung.BuildConfig
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import java.sql.Timestamp


interface DateHelper {
    fun isDateExpired(oldDate: LocalDateTime): Boolean
    fun getDateFromTimestamp(timeStampNumber: Long): LocalDateTime
    fun getDayOfWeekFromTimestamp(timeStampNumber: Long): DayOfWeek
    fun isToday(timeStampNumber: Long): Boolean
    fun isMorning(timeStampNumber: Long): Boolean
}

class DateHelperImpl : DateHelper {
    override fun isDateExpired(oldDate: LocalDateTime): Boolean =
        LocalDateTime.now() >= oldDate.plusMinutes(BuildConfig.REMOTE_CACH_MINUTES_TIME)

    override fun getDateFromTimestamp(timeStampNumber: Long): LocalDateTime =
        fromTimestamp(timeStampNumber)

    override fun getDayOfWeekFromTimestamp(timeStampNumber: Long): DayOfWeek {
        val date = fromTimestamp(timeStampNumber)
        return date.dayOfWeek
    }

    override fun isMorning(timeStampNumber: Long): Boolean =
        (LocalDateTime.now().plusDays(1).dayOfWeek == fromTimestamp(timeStampNumber).dayOfWeek)

    override fun isToday(timeStampNumber: Long): Boolean =
        (LocalDateTime.now().dayOfWeek == fromTimestamp(timeStampNumber).dayOfWeek)

    private fun fromTimestamp(timeStampNumber: Long): LocalDateTime {
        return if (timeStampNumber.toString().length < 13)
            DateTimeUtils.toLocalDateTime(Timestamp(timeStampNumber * 1000))
        else
            DateTimeUtils.toLocalDateTime(Timestamp(timeStampNumber))
    }
}
