package org.ralberth.areyouok.ui.settings.movement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import org.ralberth.areyouok.movement.MovementSource


@Composable
fun MovementUiUpdater(
    source: MovementSource,
    content: @Composable (List<Int>) -> Unit
) {
    var history by remember() { mutableStateOf(source.getHistory()) }
    content.invoke(history)

    var isVisible by remember { mutableStateOf(true) }
    LifecycleResumeEffect(Unit) {
        isVisible = true
        onPauseOrDispose { isVisible = false }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            while (true) {
                history = source.getHistory()
                delay(250)
            }
        }
    }
}
