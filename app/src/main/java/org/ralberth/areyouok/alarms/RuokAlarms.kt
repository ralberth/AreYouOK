package org.ralberth.areyouok.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.Constants.Companion.MS_PER_MIN
import javax.inject.Inject
import javax.inject.Singleton


val EXTRA_KEY_MINS_LEFT: String = "MINUTES_LEFT"


@Singleton
class RuokAlarms @Inject constructor(
    @ApplicationContext private val context: Context
) {
    init {
        println("Create RuokAlarms")
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent0: PendingIntent = _createPendingIntent(100, 0)
    val intent1: PendingIntent = _createPendingIntent(101, 1)
    val intent2: PendingIntent = _createPendingIntent(102, 2)
    val intent3: PendingIntent = _createPendingIntent(103, 3)


    fun _createPendingIntent(rqstCode: Int, minsLeft: Int): PendingIntent {
        val intent: Intent = Intent(context, RuokAlarmReceiver::class.java).apply {
            putExtra(EXTRA_KEY_MINS_LEFT, minsLeft)
        }

        return PendingIntent.getBroadcast(
            context,
            rqstCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ScheduleExactAlarm")
    fun _setAlarm(timeMS: Long, rqstCode: Int, minsLeft: Int) {
        if (canSetAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMS,
                _createPendingIntent(rqstCode, minsLeft)
            )
        } else {
            println("Can't schedule exact alarms")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S) // in order to call canScheduleExactAlarms()
    fun canSetAlarms(): Boolean {
        return alarmManager.canScheduleExactAlarms()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun setAlarms(minsInFuture: Int) {
        val now: Long = System.currentTimeMillis()
        val time0 = now + (minsInFuture * MS_PER_MIN)
        val time1 = time0 - MS_PER_MIN
        val time2 = time1 - MS_PER_MIN
        val time3 = time2 - MS_PER_MIN
        _setAlarm(time0, 100, 0)
        _setAlarm(time1, 101, 1)
        _setAlarm(time2, 102, 2)
        _setAlarm(time3, 103, 3)
    }


    fun cancelAllAlarms() {
        // .cancelAll() only for higher android API level
        alarmManager.cancel(intent0)
        alarmManager.cancel(intent1)
        alarmManager.cancel(intent2)
        alarmManager.cancel(intent3)
    }
}
