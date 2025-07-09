package org.ralberth.areyouok.movement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay


@Composable
fun MovementUiUpdater(
    source: MovementSource,
    content: @Composable (RotationPosition) -> Unit
) {
    var position by remember() { mutableStateOf(source.position()) }
    content.invoke(position)

    var isVisible by remember { mutableStateOf(true) }
    LifecycleResumeEffect(Unit) {
        isVisible = true
        source.start()
        onPauseOrDispose { isVisible = false; source.stop() }
    }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            while (true) {
                position = source.position()
                delay(500)
            }
        }
    }
}
