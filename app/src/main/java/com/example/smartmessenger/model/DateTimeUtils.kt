import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    companion object {
        private const val TIME_FORMAT = "HH:mm"
        private const val WEEK_FORMAT = "EEEE"
        private const val DATE_FORMAT_DAY_MONTH = "d MMM."
        private const val DATE_FORMAT_DAY_MONTH_YEAR = "dd MMM yy"
        private const val DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000L
        private const val WEEK_IN_MILLISECONDS = 7 * DAY_IN_MILLISECONDS
        private const val YEAR_IN_MILLISECONDS = 365 * DAY_IN_MILLISECONDS
    }

    fun formatDateTimeForChatList(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - timestamp

        return when {
            diff <= DAY_IN_MILLISECONDS -> formatDateTimeByFormat(timestamp, TIME_FORMAT)
            diff <= WEEK_IN_MILLISECONDS -> formatDateTimeByFormat(timestamp, WEEK_FORMAT)
            diff <= YEAR_IN_MILLISECONDS -> formatDateTimeByFormat(timestamp, DATE_FORMAT_DAY_MONTH)
            else -> formatDateTimeByFormat(timestamp, DATE_FORMAT_DAY_MONTH_YEAR)
        }
    }
    fun formatDateTimeForCurrentChat(timestamp: Long): String {
        return formatDateTimeByFormat(timestamp, TIME_FORMAT)
    }

    private fun formatDateTimeByFormat(timestamp: Long, format: String): String {
        val timeFormat = SimpleDateFormat(format, Locale.getDefault())
        return timeFormat.format(Date(timestamp))
    }
}
