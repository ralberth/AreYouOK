package org.ralberth.areyouok

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.ui.graphics.Color
import org.ralberth.areyouok.messages.LogMessage
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton


data class LogMessage(
    val message: String = "",
    val color: Color = Color.Black,
    val logTime: Date = Date()
)


@Singleton
class MessageLogger @Inject constructor() {
    private val messages: List<LogMessage> = ArrayList<LogMessage>()

    fun addMessage(message, color: Color = Color.Black, logTime: Date = Date()) {
        messages.add(LogMessage(message, color, logTime)
        if (newList.size >= 50)
            newList.removeAt(0)