package org.ralberth.areyouok.datamodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.StatusIdle
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RuokDatastore @Inject constructor(
    @ApplicationContext context: Context
) {
    // TODO: Deprecated, but the current docs suggest using this.  Switch later.
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    private fun dump(s: MainScreenState): String {
        val ary = arrayOf(
            "whenEnabled=${s.whenEnabled?.toString()}",
            "delayMins=${s.delayMins}",
            "message=${s.message}",
            "statusColor=${s.statusColor}",
            "minsLeft=${s.minsLeft}",
            "countdownBarColor=${s.countdownBarColor}"
        )
        return ary.joinToString(", ")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun hydrateMainScreenState(): MainScreenState {
        if (prefs.contains("delayMins"))
            println("Hydrate viewmodel: previous state found in preferences")
        else
            println("Hydrate viewmodel: brand new! No previous preferences!")


        val ret = MainScreenState(
            whenEnabled = prefs.getInstant("whenEnabled"),
            delayMins = prefs.getInt("delayMins", 20),
            message = prefs.getString("message", "Idle") ?: "Idle",
            statusColor = prefs.getColor("statusColor", StatusIdle),
            minsLeft = prefs.getInt("minsLeft", 4),
            countdownBarColor = prefs.getColor("countdownBarColor", ProgressOK)
        )
        println("Hydrated ${dump(ret)}")
        return ret
    }


    fun saveMainScreenState(state: MainScreenState) {
        println("Save state ${dump(state)}")

        with (prefs.edit()) {
            putInstant("whenEnabled", state.whenEnabled)
            putInt("delayMins", state.delayMins)
            putString("message", state.message)
            putColor("statusColor", state.statusColor)
            putInt("minsLeft", state.minsLeft)
            putColor("countdownBarColor", state.countdownBarColor)
            apply()
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun SharedPreferences.getInstant(key: String): Instant? {
    val strVal = this.getString(key, null)
    return if (strVal != null) Instant.parse(strVal) else null
}

private fun SharedPreferences.Editor.putInstant(key: String, inst: Instant?) {
    putString(key, inst?.toString())
}


private fun SharedPreferences.getColor(key: String, defaultValue: Color): Color {
    val strVal = this.getString(key, null)
    return if (strVal != null) Color(strVal.toULong()) else defaultValue
}

private fun SharedPreferences.Editor.putColor(key: String, color: Color?) {
    val strVal: String? = if (color != null) color.value.toString() else null
    putString(key, strVal)
}
