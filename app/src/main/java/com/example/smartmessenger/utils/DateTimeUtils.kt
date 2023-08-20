package com.example.smartmessenger.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    private const val TIME_FORMAT = "HH:mm"
    private const val WEEK_FORMAT = "EEE"
    private const val DATE_FORMAT_DAY_MONTH = "d MMM."
    private const val DATE_FORMAT_DAY_MONTH_YEAR = "dd MMM yy"
    private const val DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000L
    private const val WEEK_IN_MILLISECONDS = 7 * DAY_IN_MILLISECONDS
    private const val YEAR_IN_MILLISECONDS = 365 * DAY_IN_MILLISECONDS

    fun Date.formatDateTimeForChatList(): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - this.time

        return when {
            diff <= DAY_IN_MILLISECONDS -> formatDateTimeByFormat(this, TIME_FORMAT)
            diff <= WEEK_IN_MILLISECONDS -> formatDateTimeByFormat(this, WEEK_FORMAT)
            diff <= YEAR_IN_MILLISECONDS -> formatDateTimeByFormat(this, DATE_FORMAT_DAY_MONTH)
            else -> formatDateTimeByFormat(this, DATE_FORMAT_DAY_MONTH_YEAR)
        }
    }

    fun Date.formatDateTimeForCurrentChat(): String {
        return formatDateTimeByFormat(this, TIME_FORMAT)
    }

    private fun formatDateTimeByFormat(timestamp: Date, format: String): String {
        val timeFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return timeFormat.format(timestamp).lowercase()
    }
}
