package org.ralberth.areyouok.datamodel

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
    // TODO: Deprecated, but the current docs suggest using this.  Switch later.
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    private fun dump(s: MainScreenState): String {
        val ary = arrayOf(
            "countdownStart=${s.countdownStart?.toString()}",
            "countdownStop=${s.countdownStop?.toString()}",
            "countdownLength=${s.countdownLength}",
            "minsLeft=${s.minsLeft}"
        )
        return ary.joinToString(", ")
    }


    fun hydrateMainScreenState(): MainScreenState {
        if (prefs.contains("delayMins"))
            println("Hydrate viewmodel: previous state found in preferences")
        else
            println("Hydrate viewmodel: brand new! No previous preferences!")

        // FIXME: not sure why we can't just prefs.getInt("minsLeft", null) below...
        var minsLeft: Int? = prefs.getInt("minsLeft", -1)
        if (minsLeft == -1) minsLeft = null

        val ret = MainScreenState(
            countdownStart = prefs.getInstant("countdownStart"),
            countdownStop = prefs.getInstant("countdownStop"),
            countdownLength = prefs.getInt("countdownLength", 30),
            minsLeft = minsLeft
        )
        println("Hydrated ${dump(ret)}")
        return ret
    }


    fun saveMainScreenState(state: MainScreenState) {
        println("Save state ${dump(state)}")

        with (prefs.edit()) {
            putInstant("countdownStart", state.countdownStart)
            putInstant("countdownStop", state.countdownStop)
            putInt("countdownLength", state.countdownLength)
            putInt("minsLeft", if (state.minsLeft != null) state.minsLeft else -1)
            apply()
        }
    }
}


private fun SharedPreferences.getInstant(key: String): Instant? {
    val strVal = this.getString(key, null)
    return if (strVal != null) Instant.parse(strVal) else null
}


private fun SharedPreferences.Editor.putInstant(key: String, inst: Instant?) {
    putString(key, inst?.toString())
}


//private fun SharedPreferences.getColor(key: String, defaultValue: Color): Color {
//    val strVal = this.getString(key, null)
//    return if (strVal != null) Color(strVal.toULong()) else defaultValue
//}
//
//
//private fun SharedPreferences.Editor.putColor(key: String, color: Color?) {
//    val strVal: String? = color?.value?.toString()
//    putString(key, strVal)
//}
