package org.ralberth.areyouok.ui.mainscreen

import java.time.Instant
import org.junit.Assert.*
import org.junit.Test
import org.ralberth.areyouok.ui.coutdownscreen.duration2humanreadable
import org.ralberth.areyouok.ui.coutdownscreen.instant2humanReadableTime
import java.time.Duration
import java.time.ZoneOffset


class CountdownDisplayTest {
    fun testInstant2humanReadableTime(time: String?, expected: String) {
        val i = if (time != null) Instant.parse(time) else null
        assertEquals(expected, instant2humanReadableTime(i, ZoneOffset.UTC))
    }

    @Test
    fun instant2humanReadableTime_null() {
        testInstant2humanReadableTime(null, "")
    }

    @Test
    fun instant2humanReadableTime_am() {
        testInstant2humanReadableTime("2025-01-01T09:12:34Z", "09:12")
    }

    @Test
    fun instant2humanReadbleTime_pm() {
        testInstant2humanReadableTime("2025-01-01T17:12:34Z", "05:12")
    }

    // ----------------------------------------------------------------------------

    fun testDuration2humanreadable(isoDurationStr: String?, expected: String) {
        val d: Duration? = if (isoDurationStr != null) Duration.parse(isoDurationStr) else null
        assertEquals(expected, duration2humanreadable(d))
    }

    @Test
    fun duration2humanreadable_null() {
        testDuration2humanreadable(null, "")
    }

    @Test
    fun duration2humanreadable_0s() {
        testDuration2humanreadable("PT0s", "0s")
    }

    @Test
    fun duration2humanreadable_9s() {
        testDuration2humanreadable("PT9s", "9s")
    }

    @Test
    fun duration2humanreadable_1m8s() {
        testDuration2humanreadable("PT1m8s","1m8s")
    }

    @Test
    fun duration2humanreadable_43m59s() {
        testDuration2humanreadable("PT43m59s","43m59s")
    }

    @Test
    fun duration2humanreadable_56m0s() {
        testDuration2humanreadable("PT56m", "56m0s")
    }

    @Test
    fun duration2humanreadable_1h0m0s() {
        testDuration2humanreadable("PT1h", "1h0m0s")
    }

    @Test
    fun duration2humanreadable_9h0m1s() {
        testDuration2humanreadable("PT9h1s", "9h0m1s")
    }
}
