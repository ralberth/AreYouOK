package org.ralberth.areyouok.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import org.ralberth.areyouok.R
import org.ralberth.areyouok.R.mipmap.ic_launcher_round


class RuokChannel(
    private val context: Context,
    private val channelId: String,
    channelName: String,
    isHighImportance: Boolean = true,
    bypassDoNotDisturb: Boolean = true,
    soundResourceId: Int = R.raw.silent
) {
    private val bitmap = BitmapFactory.decodeResource(context.resources, ic_launcher_round)
    val notificationMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private val importance: Int = if (isHighImportance)
        NotificationManager.IMPORTANCE_HIGH
    else
        NotificationManager.IMPORTANCE_DEFAULT

    private val priority: Int = if (isHighImportance)
        NotificationCompat.PRIORITY_HIGH
    else
        NotificationCompat.PRIORITY_DEFAULT

    private fun buildSoundUri(i: Int): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(i))
            .appendPath(context.resources.getResourceTypeName(i))
            .appendPath(context.resources.getResourceEntryName(i))
            .build()
    }

    private val audioAttrs = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val channel = NotificationChannel(channelId, channelName, importance).apply {
        this.setBypassDnd(bypassDoNotDisturb)
        this.setSound(buildSoundUri(soundResourceId), audioAttrs)
    }

    init {
        notificationMgr.createNotificationChannel(channel)
    }


    fun sendNotification(
        messageId: Int,
        title: String,
        message: String,
        intent: PendingIntent? = null
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(bitmap)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priority)

        if (intent != null)
            builder.setContentIntent(intent)

        println("RuokNotifier.sendNotification(\"$channelId\", \"$message\")")

        notificationMgr.notify(messageId, builder.build())
    }
}
