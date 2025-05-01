package org.ralberth.areyouok.datamodel

import java.time.Instant


data class MainScreenState(
    // Big settings that remain unchanged while the app is counting down
    val countdownStart: Instant?,   // Timestamp when user turned on the countdown (null if not)
    val countdownStop: Instant?,    // Timestamp when the coutdown expires
    val countdownLength: Int,       // Number of minutes to countdown (for UI select)

    // Tactical stuff that changes while the countdown is running
    val minsLeft: Int?              // Minutes left before timeToStop, for progressbar display
)
