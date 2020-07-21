package com.mohsen.apk.wetterkleidung.utility.date

import com.mohsen.apk.wetterkleidung.BuildConfig
import org.threeten.bp.LocalDateTime

interface DateHelper {
    fun is30MinExpired(oldDate: LocalDateTime): Boolean
}

class DateHelperImpl : DateHelper {
    override fun is30MinExpired(oldDate: LocalDateTime): Boolean =
        LocalDateTime.now() >= oldDate.plusMinutes(BuildConfig.REMOTE_CACH_MINUTES_TIME)

}
