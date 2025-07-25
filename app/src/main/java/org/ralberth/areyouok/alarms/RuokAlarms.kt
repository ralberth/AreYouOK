package org.ralberth.areyouok.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.RuokIntents
import javax.inject.Inject
import javax.inject.Singleton


const val MS_PER_MIN: Long = 60 * 1000


@Singleton
class RuokAlarms @Inject constructor(
    @ApplicationContext private val context: Context,
    private val intentGenerator: RuokIntents
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(timeMS: Long, minsLeft: Int) {
        val pi = intentGenerator.createMinsLeftPendingIntent(minsLeft)
        if (canSetAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMS, pi)
        } else {
            println("Can't schedule exact alarms")
        }
    }


    fun canSetAlarms(): Boolean {
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
        // There's never more than T-3, T-2, T-1, and T-0 Alarms at any one time.
        // Just cancel all of them every time.
        for (minsLeft in intArrayOf(3, 2, 1, 0)) {
            alarmManager.cancel(intentGenerator.createMinsLeftPendingIntent(minsLeft))
        }
    }
}
