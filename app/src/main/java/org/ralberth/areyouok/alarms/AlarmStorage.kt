package org.ralberth.areyouok.alarms

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/*
 * Store and retrieve PendingIntents in permanent storage that survives Task termination.
 * Unfortunately, AlarmManager.cancelAll() is not available in this version of the Androi SDK.
 *
 * This is a major problem, because of this sequence:
 *     1. Run RUOK
 *     2. Start the countdown (new PendingIntents sent to AlarmManager at T-3, T-2, ...)
 *     3. Quit RUOK
 *     4. Run RUOK
 *     5. Stop the countdown (which should cancel all AlarmManager PendingIntents from 2 above)
 *
 * The problem is that we have to maintain our own list of the PendingIntents we give to
 * AlarmManager.  If we keep them inside the app (like a local List<PendingIntent> inside our
 * RuokAlarms class), then step 5 above has no saved PendingIntents!  The PendingIntents created
 * in step 2 above are still around, but we have lost all connection to them and can't cancel them.
 *
 * SOLUTION: persist all PendingIntents in a place that survives complete termination of RUOK:
 * the phone's preferences store.  PendingIntents are Parseable, so they can be serialized and
 * rehydrated easily.  HOWEVER, this isn't the simplest way to do this.
 *
 * The Android OS has specific rules for determining if two PendingIntents or Intents are the
 * "same".  It compares only a few things and ignores other things like Extras.
 * From the API docs:
 *       > The parts of the Intent that are used for matching are the
 *       > same ones defined by Intent.filterEquals()
 * These fields are: action, data, type, identity, class, and categories.
 * So just persist these portions of a PendingIntent and recreate them based only on these
 * fields.  PROBLEM: this means you LOSE ALL EXTRAS in the original PendingIntent!  This
 * doesn't matter for our use case, but we'll protect against this in the methods below.
 *
 * To make things even WORSE, this class can't take a PendingIntent as a parameter to a function
 * and pull out any of these fields!  PendingIntent is a WRITE-ONLY interface by design.
 * SO, we have to accept the individual pieces to store in SharedPreferences and create
 * PendingIntents on umarshall.  This means this has to be kept in sync with the PIs created from
 * methods in RuokIntents.  LOSE LOSE Android!
 */
@Singleton
class AlarmStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        const val KEY_NAME = "alarmPendingIntents"
    }

    // TODO: Deprecated, but the current docs suggest using this.  Switch later.
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val gson = Gson()


//    fun addPendingIntent(clazz: Class<RuokAlarmReceiver>, requestCode: Int) {
//        val jsonPi = marshallPendingIntentToJson(pi)
//        val currentPIs = prefs.getStringSet(KEY_NAME, setOf<String>()) ?: setOf<String>()
//        val newPIs = currentPIs.plus(jsonPi)
//        with (prefs.edit()) {
//            putStringSet(KEY_NAME, newPIs)
//            apply()
//        }
//        println("Added PendingIntent to AlarmStorage: $jsonPi")
//        println("All PendingIntents in AlarmStorage:")
//        for (piStr in newPIs)
//            println("PI: $piStr")
//    }
//
//
//    private fun marshallPendingIntentToJson(pi: PendingIntent): String {
//        val marshalledObj = mapOf(
//            "action" to pi.,
//            "data" to pi.data,
//            "type" to null,
//            "identity" to null,
//            "class" to null,
//            "categories" to null
//        )
//        return gson.toJson(marshalledObj)
//    }
}