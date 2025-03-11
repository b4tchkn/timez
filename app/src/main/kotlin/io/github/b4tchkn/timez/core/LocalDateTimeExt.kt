package io.github.b4tchkn.timez.core

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun LocalDateTime.formatRelativeTimeFromNow(
    nowDateTime: LocalDateTime,
): RelativeTime {
    val diff = nowDateTime
        .toInstant(TimeZone.currentSystemDefault())
        .minus(this.toInstant(TimeZone.currentSystemDefault()))

    return when {
        diff.inWholeDays > 0 -> {
            RelativeTime.Days(diff.inWholeDays)
        }

        else -> {
            RelativeTime.Hours(diff.inWholeHours)
        }
    }
}

sealed class RelativeTime {
    data class Days(
        val days: Long,
    ) : RelativeTime()

    data class Hours(
        val hours: Long,
    ) : RelativeTime()
}
