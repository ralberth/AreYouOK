package org.ralberth.areyouok.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CenteredButton(
    onClick: () -> Unit = {},
    label: String = "OK",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onClick) {
            Text(label)
        }
    }
}