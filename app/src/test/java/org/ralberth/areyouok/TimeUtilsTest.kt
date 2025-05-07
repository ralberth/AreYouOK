package org.ralberth.areyouok

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class TimeUtilsTest {
    fun testMinutesBeforeEnd(endStr: String?, nowStr: String, expected: Int?) {
        val end: Instant? = if (endStr != null) Instant.parse(endStr) else null
        val now: Instant = Instant.parse(nowStr)
        assertEquals(expected, minutesBeforeEnd(end, now))
    }

    @Test
    fun minutesBeforeEnd_null() {
        testMinutesBeforeEnd(null, "2024-12-31T09:15:00Z", null)
    }

    @Test
    fun minutesBeforeEnd_now_equals_end() {
        testMinutesBeforeEnd("2024-12-31T09:15:00Z", "2024-12-31T09:15:00Z", 0)
    }

    @Test
    fun minutesBeforeEnd_now_after_end() {
        testMinutesBeforeEnd("2024-12-31T09:15:00Z", "2025-12-31T09:15:00Z", 0)
    }

    @Test
    fun minutesBeforeEnd_6m1s() {
        testMinutesBeforeEnd("2021-08-10T14:06:01Z", "2021-08-10T14:00:00Z", 7)
    }

    @Test
    fun minutesBeforeEnd_6m0s() {
        testMinutesBeforeEnd("2021-08-10T14:06:00Z", "2021-08-10T14:00:00Z", 6)
    }

    @Test
    fun minutesBeforeEnd_5m59s() {
        testMinutesBeforeEnd("2021-08-10T14:05:59Z", "2021-08-10T14:00:00Z", 6)
    }

    @Test
    fun minutesBeforeEnd_1m1s() {
        testMinutesBeforeEnd("2021-08-10T14:01:01Z", "2021-08-10T14:00:00Z", 2)
    }

    @Test
    fun minutesBeforeEnd_1m0s() {
        testMinutesBeforeEnd("2021-08-10T14:01:00Z", "2021-08-10T14:00:00Z", 1)
    }

    @Test
    fun minutesBeforeEnd_59s() {
        testMinutesBeforeEnd("2021-08-10T14:00:59Z", "2021-08-10T14:00:00Z", 1)
    }

    @Test
    fun minutesBeforeEnd_1s() {
        testMinutesBeforeEnd("2021-08-10T14:00:01Z", "2021-08-10T14:00:00Z", 1)
    }


    // ---------------------------------------------------------------------------------------


    fun testProgressPercent(startStr: String?, endStr: String?, nowStr: String, expected: Float) {
        val start = if (startStr != null) Instant.parse(startStr) else null
        val end   = if (endStr   != null) Instant.parse(endStr)   else null
        val now   = Instant.parse(nowStr)
        val actual: Float = progressPercent(start, end, now)
        assertEquals(expected, actual, 0.01f)  //  Pct can be off by +/- 1%
    }

    @Test
    fun progressPercent_allnull() {
        testProgressPercent(null, null, "2025-01-01T10:11:12Z", 0f)
    }

    @Test
    fun progressPercent_startnull() {
        testProgressPercent(null, "2025-01-01T10:11:12Z", "2025-01-01T10:11:12Z", 0f)
    }

    @Test
    fun progressPercent_endnull() {
        testProgressPercent("2025-01-01T10:11:12Z", null, "2025-01-01T10:11:12Z", 0f)
    }

    @Test
    fun progressPercent_start_after_now() {
        testProgressPercent("2025-01-03T10:11:12Z", "2025-01-04T10:11:12Z", "2025-01-02T10:11:12Z", 0f)
    }

    @Test
    fun progressPercent_now_after_end() {
        testProgressPercent("2025-01-03T10:11:12Z", "2025-01-04T10:11:12Z","2025-01-05T10:11:12Z", 1f)
    }

    @Test
    fun progressPercent_now_equals_start() {
        testProgressPercent("2025-01-03T10:11:12Z", "2025-01-04T10:11:12Z","2025-01-03T10:11:12Z", 0f)
    }

    @Test
    fun progressPercent_now_equals_end() {
        testProgressPercent("2025-01-03T10:11:12Z", "2025-01-04T10:11:12Z","2025-01-04T10:11:12Z", 1f)
    }

    @Test
    fun progressPercent_8pct() {
        testProgressPercent("2025-01-03T10:00:00Z", "2025-01-03T11:40:00Z","2025-01-03T10:08:00Z", 0.08f)
    }

    @Test
    fun progressPercent_25pct() {
        testProgressPercent("2025-01-03T10:00:00Z", "2025-01-03T11:40:00Z","2025-01-03T10:25:00Z", 0.25f)
    }

    @Test
    fun progressPercent_98pct() {
        testProgressPercent("2025-01-03T10:00:00Z", "2025-01-03T11:40:00Z","2025-01-03T11:38:00Z", 0.98f)
    }
}