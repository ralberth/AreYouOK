package org.ralberth.areyouok

import java.time.Duration
import java.time.Instant


fun minutesBeforeEnd(d: Duration): Int {
    // Stay in integer domain for simplicity: we want ceil() but the "/"
    // operator is "floor" semantics.
    if (d.isNegative)
        return 0
    return ((d.seconds + 59L) / 60L).toInt()
}

fun minutesBeforeEnd(end: Instant?, now: Instant = Instant.now()): Int? {
    if (end == null)
        return null

    val d: Duration = Duration.between(now, end)
    if (d.isNegative)
        return 0
    return minutesBeforeEnd(d)
}


fun progressPercent(start: Instant?, end: Instant?, now: Instant = Instant.now()): Float {
    if (start == null || end == null)
        return 0f

    if (now.isBefore(start))
        return 0f

    if (now.isAfter(end))
        return 1f

    val range:    Duration = Duration.between(start, end)
    val progress: Duration = Duration.between(start, now)
    return progress.seconds.toFloat() / range.seconds.toFloat()
}
