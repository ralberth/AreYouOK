package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Instant
import org.ralberth.areyouok.ui.theme.StatusTextIdle
import org.ralberth.areyouok.ui.theme.StatusTextWarning
import org.ralberth.areyouok.ui.theme.StatusTextDanger
import org.ralberth.areyouok.ui.theme.StatusTextPaging
import org.ralberth.areyouok.ui.theme.StatusTextRunning
import org.ralberth.areyouok.minutesBeforeEnd


@Composable
fun StatusDisplayText(endTime: Instant?) {
    val minsLeft = minutesBeforeEnd(endTime)
    val message: String = when(minsLeft) {
        null -> ""
        3    -> "Running out of time"
        2    -> "Running out of time!"
        1    -> "ONE MINUTE LEFT"
        0    -> "ALARM - notifying contacts"
        else -> "Running"
    }

    val textColor: Color = when(minsLeft) {
        null -> StatusTextIdle
        3    -> StatusTextWarning
        2    -> StatusTextWarning
        1    -> StatusTextDanger
        0    -> StatusTextPaging
        else -> StatusTextRunning
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            message,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
