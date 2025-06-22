package org.ralberth.areyouok.messaging


/*
 * Not Composable, not a UI, not singleton, not anything.
 * This is just a place to get strings formatted for sending as TXT messages.
 * This could be resources in the app, but that would mean every use would
 * need to know what arguments to substitute, which isn't an abstraction.
 */
class RuokMessageStrings {
    companion object {
        fun getTurnedOnMessage(mins: Int, location: String): String {
            return "⚪ Alerting turned on.  Check-ins every $mins minutes.  Current location: $location"
        }

        fun getTurnedOffMessage(): String {
            return "⚫ Alerting turned off."
        }

        fun getDurationChangedMessage(newDuration: Int, checkinTime: String): String {
            return "⏱️ Check-ins are now every $newDuration minutes.  Next check-in at $checkinTime"
        }

        fun getLocationChangedMessage(newLocation: String): String {
            return "🆕📍🗺️ New location: $newLocation"
        }

        fun getCheckinMessage(nextCheckin: String): String {
            return "👍 Check-in!  Next check-in at $nextCheckin"
        }

        fun missedCheckinMessage(location: String): String {
            return "🚨 MISSED LAST CHECK-IN 🚨  Last known location: $location"
        }
    }
}
