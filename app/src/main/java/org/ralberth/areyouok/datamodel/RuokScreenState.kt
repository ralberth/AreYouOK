package org.ralberth.areyouok.datamodel

import java.time.Instant


data class RuokScreenState(
    val countdownStart: Instant?,       // Timestamp when user turned on the countdown (null if not)
    val countdownStop: Instant?,        // Timestamp when the countdown expires
    val countdownLength: Int,           // Number of minutes to countdown (for UI select)

    val phoneName: String,              // Display name of human contact
    val phoneNumber: String,            // Phone number in whatever format we get from the phone

    val location: String,               // Description of where the user physically is
    val recentLocations: List<String>,  // sorted so earlier was selected more recently

    val volumePercent: Float?,          // Percentage (0..1) of max volume alerts and notifications should play at

    val foregroundOnAlerts: Boolean,    // If the app should become visible when alerts are sent

    val alarmOnNoMovement: Boolean,     // If app should pay attention to the accelerometer
    val movementThreshold: Int          // threshold over which we consider the cell phone in motion
) {
    fun isCountingDown(): Boolean {
        return countdownStart != null
    }
}
