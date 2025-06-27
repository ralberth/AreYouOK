package org.ralberth.areyouok.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun SettingsRow(
    label: String,
    value: String,
    onEdit: () -> Unit = { },
    leftIcon: ImageVector? = null,
    canEdit: Boolean = true,
    description: String? = null
) {
    Column(
        modifier = Modifier.padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(canEdit, onClickLabel = "click", onClick = onEdit),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leftIcon != null)
                Icon(
                    leftIcon,
                    "left",
                    modifier = Modifier.padding(end = 18.dp)
                )
            Column {
                Text(label, fontWeight = FontWeight.Bold)
                Text(value)
            }
            if (canEdit) {
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Change")
            }
        }
        if (description != null)
            Text(
                text = description,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 10.dp)
            )
    }
}
