package org.ralberth.areyouok.datamodel

import java.time.Instant


data class RuokScreenState(
    val countdownStart: Instant?,   // Timestamp when user turned on the countdown (null if not)
    val countdownStop: Instant?,    // Timestamp when the coutdown expires
    val countdownLength: Int,       // Number of minutes to countdown (for UI select)
    val phoneName: String,
    val phoneNumber: String,
    val location: String
)
