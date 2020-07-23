package com.mohsen.apk.wetterkleidung.utility.date

import com.mohsen.apk.wetterkleidung.BuildConfig
import org.threeten.bp.LocalDateTime

interface DateHelper {
    fun isDateExpired(oldDate: LocalDateTime): Boolean
}

class DateHelperImpl : DateHelper {
    override fun isDateExpired(oldDate: LocalDateTime): Boolean =
        LocalDateTime.now() >= oldDate.plusMinutes(BuildConfig.REMOTE_CACH_MINUTES_TIME)

}
