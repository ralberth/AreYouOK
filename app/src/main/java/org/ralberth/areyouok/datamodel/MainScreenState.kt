package org.ralberth.areyouok.datamodel

import androidx.compose.ui.graphics.Color


data class MainScreenState(
    // Big settings that remain unchanged while the app is counting down
    val enabled: Boolean,
    val delayMins: Int,

    // Tactical stuff that changes while the timer is running
    val message: String,
    val statusColor: Color,
    val minsLeft: Int,
    val countdownBarColor: Color
)
