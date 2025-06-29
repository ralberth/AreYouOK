package org.ralberth.areyouok.datamodel

import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RuokDatastore @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        val NEW_RECENT_LOCS = listOf("Home", "Office", "Gym")
    }

    // TODO: Deprecated, but the current docs suggest using this.  Switch later.
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    private fun dump(s: RuokScreenState): String {
        val ary = arrayOf(
            "countdownStart=${s.countdownStart?.toString()}",
            "countdownStop=${s.countdownStop?.toString()}",
            "countdownLength=${s.countdownLength}",
            "phoneName=${s.phoneName}",
            "phoneNumber=${s.phoneNumber}",
            "location=${s.location}",
            "recentLocations=${s.recentLocations.joinToString("; ") }",
            "volumePercent=${s.volumePercent ?: "(use phone volume)"}",
            "foregroundOnAlerts=${s.foregroundOnAlerts}"
        )
        return ary.joinToString(", ")
    }


    fun hydrateMainScreenState(): RuokScreenState {
        val ret = RuokScreenState(
            countdownStart = prefs.getInstant("countdownStart"),
            countdownStop = prefs.getInstant("countdownStop"),
            countdownLength = prefs.getInt("countdownLength", 30),
            phoneName = prefs.getString("phoneName", "") ?: "",
            phoneNumber = prefs.getString("phoneNumber", "") ?: "",
            location = prefs.getString("location", "") ?: "",
            recentLocations = prefs.getStringList("recentLocations", NEW_RECENT_LOCS),
            volumePercent = if (prefs.contains("volumePercent")) prefs.getFloat("volumePercent", 5f) else null,
            foregroundOnAlerts = prefs.getBoolean("foregroundOnAlerts", true)
        )
        println("Hydrated ${dump(ret)}")
        return ret
    }


    fun saveMainScreenState(state: RuokScreenState) {
        println("Save state ${dump(state)}")
        with (prefs.edit()) {
            putInstant("countdownStart", state.countdownStart)
            putInstant("countdownStop", state.countdownStop)
            putInt("countdownLength", state.countdownLength)
            putString("phoneName", state.phoneName)
            putString("phoneNumber", state.phoneNumber)
            putString("location", state.location)
            putStringList("recentLocations", state.recentLocations)
            if (state.volumePercent != null)
                putFloat("volumePercent", state.volumePercent)
            else
                remove("volumePercent")
            putBoolean("foregroundOnAlerts", state.foregroundOnAlerts)
            apply()
        }
    }


    fun getPhoneNumber(): String {
        return prefs.getString("phoneNumber", "") ?: ""
    }


    fun getLocation(): String {
        return prefs.getString("location", "") ?: ""
    }


    fun getVolumePercent(): Float? {
        return if (prefs.contains("volumePercent"))
            prefs.getFloat("volumePercent", 0f)
        else
            null
    }

    fun foregroundOnAlerts(): Boolean {
        return prefs.getBoolean("foregroundOnAlerts", true)
    }
}


private fun SharedPreferences.getInstant(key: String): Instant? {
    val strVal = this.getString(key, null)
    return if (strVal != null) Instant.parse(strVal) else null
}


private fun SharedPreferences.Editor.putInstant(key: String, inst: Instant?) {
    putString(key, inst?.toString())
}

private fun SharedPreferences.getStringList(key: String, default: List<String>): List<String> {
    val str = this.getString(key, null)
    if (str == null)
        return default
    val rawStrList = str.split("\n").filterNot({ it == null || it.isBlank() })
    return rawStrList.ifEmpty { default }
}

private fun SharedPreferences.Editor.putStringList(key: String, sl: List<String>) {
    val nonEmptyList = sl.filterNot({ it == null || it.isBlank() })
    putString(key, nonEmptyList.joinToString("\n"))
}