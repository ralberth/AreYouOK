package org.ralberth.areyouok.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.Constants.Companion.MS_PER_MIN
import javax.inject.Inject
import javax.inject.Singleton


const val REQUEST_CODE = 101
const val EXTRA_KEY_MINS_LEFT = "MINUTES_LEFT"


@Singleton
class RuokAlarms @Inject constructor(
    @ApplicationContext private val context: Context
) {
    init {
        println("Create RuokAlarms")
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var pendingIntents = emptyArray<PendingIntent>()


    private fun createPendingIntent(minsLeft: Int, target: Class<RuokAlarmReceiver>): PendingIntent {
        val intent: Intent = Intent(context, target).apply {
            putExtra(EXTRA_KEY_MINS_LEFT, minsLeft)
        }

        val pi = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        pendingIntents += pi
        return pi
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(timeMS: Long, minsLeft: Int) {
        if (canSetAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMS,
                createPendingIntent(minsLeft, RuokAlarmReceiver::class.java)
            )
        } else {
            println("Can't schedule exact alarms")
        }
    }


    private fun canSetAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            alarmManager.canScheduleExactAlarms()
        else
            true  // prior to Snow Cone, everything can schedule exact alarms
    }


    fun setAlarms(minsInFuture: Int) {
        val now: Long = System.currentTimeMillis()
        val time0 = now + (minsInFuture * MS_PER_MIN)
        val time1 = time0 - MS_PER_MIN
        val time2 = time1 - MS_PER_MIN
        val time3 = time2 - MS_PER_MIN
        setAlarm(time0, 0)
        setAlarm(time1, 1)
        setAlarm(time2, 2)
        setAlarm(time3, 3)
    }


    fun cancelAllAlarms() {
        for (pi in pendingIntents)
            alarmManager.cancel(pi)
        pendingIntents = emptyArray<PendingIntent>()
    }
}
