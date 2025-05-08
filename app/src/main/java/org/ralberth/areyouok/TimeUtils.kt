package org.ralberth.areyouok

import java.time.Duration
import java.time.Instant


//fun minutesBeforeEnd(end: Instant?, now: Instant = Instant.now()): Int? {
fun minutesBeforeEnd(end: Instant?, now: Instant? = null): Int? {
    val n = now ?: Instant.now()

    if (end == null)
        return null

    val d: Duration = Duration.between(n, end)
    if (d.isNegative)
        return 0

    // Stay in integer domain for simplicity: we want ceil() but the "/"
    // operator is "floor" semantics.
    return ((d.seconds + 59L) / 60L).toInt()
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