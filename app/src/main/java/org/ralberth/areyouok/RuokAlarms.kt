package org.ralberth.areyouok

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


// val MINUTE: Long = 60000  // 60 seconds, use for production
val MINUTE: Long = 10000 // 10 seconds, use this only for testing


@Singleton
class RuokAlarms @Inject constructor(
    @ApplicationContext private val context: Context
) {
    init {
        print("Create RuokAlarms singleton")
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent0: PendingIntent = _createPendingIntent(100, "send SMS")
    val intent1: PendingIntent = _createPendingIntent(101, "1 minute left")
    val intent2: PendingIntent = _createPendingIntent(102, "2 minutes left")
    val intent3: PendingIntent = _createPendingIntent(103, "3 minutes left")


    fun _createPendingIntent(rqstCode: Int, message: String): PendingIntent {
        // Our intent is to call RuokAlarmReceiver and tell it "ALARM_MSG"=message
        val intent: Intent = Intent(context, RuokAlarmReceiver::class.java).apply {
            putExtra("ALARM_MSG", message)
        }

        return PendingIntent.getBroadcast(
            context,
            rqstCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    fun _setAlarm(timeMS: Long, rqstCode: Int, message: String) {
        if (canSetAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeMS,
                _createPendingIntent(rqstCode, message)
            )
        } else {
            println("Can't schedule exact alarms")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S) // in order to call canScheduleExactAlarms()
    fun canSetAlarms(): Boolean {
        return alarmManager.canScheduleExactAlarms()
    }

    fun setAlarms(minsInFuture: Int) {
        val now: Long = System.currentTimeMillis()
        val time0 = now + (minsInFuture * MINUTE)
        val time1 = time0 - MINUTE
        val time2 = time1 - MINUTE
        val time3 = time2 - MINUTE
        _setAlarm(time0, 100, "Send text alerts, alarm ran out")
        _setAlarm(time1, 101, "one minute left")
        _setAlarm(time2, 102, "two minutes left")
        _setAlarm(time3, 103, "three minutes left")
    }

    fun cancelAllAlarms() {
        // .cancelAll() only for higher android API level
        alarmManager.cancel(intent0)
        alarmManager.cancel(intent1)
        alarmManager.cancel(intent2)
        alarmManager.cancel(intent3)
    }
}
