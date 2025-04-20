package org.ralberth.areyouok.datamodel

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.StatusIdle
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RuokDatastore @Inject constructor(
    @ApplicationContext context: Context
) {
    // Deprecated, but the current docs suggest using this.  Switch later.
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun hydrateMainScreenState(): MainScreenState {
        if (prefs.contains("enabled"))
            println("Hydrate viewmodel: previous state found in preferences")
        else
            println("Hydrate viewmodel: brand new! No previous preferences!")

        return MainScreenState(
            enabled = prefs.getBoolean("enabled", false),
            delayMins = prefs.getInt("delayMins", 20),
            message = prefs.getString("message", null) ?: "Idle",
            statusColor = Color(prefs.getLong("statusColor", StatusIdle.value.toLong())),
            minsLeft = prefs.getInt("minsLeft", 4),
            countdownBarColor = Color(prefs.getLong("countdownBarColor", ProgressOK.value.toLong()))
        )
    }

    fun saveMainScreenState(state: MainScreenState): Unit {
        println("Save state (enabled=${state.enabled}, minsLeft=${state.minsLeft})")
        with (prefs.edit()) {
            putBoolean("enabled", state.enabled)
            putInt("delayMins", state.delayMins)
            putString("message", state.message)
            putLong("statusColor", state.statusColor.value.toLong())
            putInt("minsLeft", state.minsLeft)
            putLong("countdownBarColor", state.countdownBarColor.value.toLong())
            apply()
        }
    }
}
