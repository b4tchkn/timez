package io.github.b4tchkn.timez.core

import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class LocalDateTimeExtTest {
    @Test
    fun `diff days`() {
        val dateTime1 = LocalDateTime(2021, 1, 1, 0, 0)
        val dateTime2 = LocalDateTime(2021, 1, 10, 0, 0)

        val diff = dateTime1.formatRelativeTimeFromNow(dateTime2)

        assertEquals(RelativeTime.Days(9), diff)
    }

    @Test
    fun `diff hours`() {
        val dateTime1 = LocalDateTime(2021, 1, 1, 0, 0)
        val dateTime2 = LocalDateTime(2021, 1, 1, 10, 0)

        val diff = dateTime1.formatRelativeTimeFromNow(dateTime2)

        assertEquals(RelativeTime.Hours(10), diff)
    }

    @Test
    fun `same date time`() {
        val dateTime1 = LocalDateTime(2021, 1, 1, 0, 0)
        val dateTime2 = LocalDateTime(2021, 1, 1, 0, 0)

        val diff = dateTime1.formatRelativeTimeFromNow(dateTime2)

        assertEquals(RelativeTime.Hours(0), diff)
    }
}
