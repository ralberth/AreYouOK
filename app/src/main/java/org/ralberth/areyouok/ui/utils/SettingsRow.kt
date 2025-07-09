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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun BasicSettingsRow(
    leftIcon: ImageVector? = null,
    label: String,
    value: String?,
    rowIsClickable: Boolean = true,
    onClickRow: () -> Unit = { },
    description: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(rowIsClickable, onClickLabel = "click", onClick = onClickRow),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leftIcon != null) {
                Icon(
                    leftIcon,
                    "left",
                    modifier = Modifier.padding(end = 18.dp)
                )
            }

            Column {
                Text(label, fontWeight = FontWeight.Bold)
                if (value != null) {
                    Text(
                        text = value,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            content()
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



@Composable
fun NavSettingsRow(
    leftIcon: ImageVector? = null,
    label: String,
    value: String? = null,
    rowIsClickable: Boolean = true,
    onClickRow: () -> Unit = { },
    description: String? = null
) {
    BasicSettingsRow(
        leftIcon = leftIcon,
        label = label,
        value = value,
        rowIsClickable = rowIsClickable,
        onClickRow = onClickRow,
        description = description
    ) {
        if (rowIsClickable)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Change")
    }
}


@Composable
fun ToggleSettingsRow(
    leftIcon: ImageVector? = null,
    label: String,
    value: String? = null,
    toggleEnabled: Boolean = true,
    isSwitchedOn: Boolean,
    onToggle: (Boolean) -> Unit,
    description: String? = null
) {
    BasicSettingsRow(
        leftIcon = leftIcon,
        label = label,
        value = value,
        rowIsClickable = false,
        onClickRow = {},
        description = description
    ) {
        Switch(
            enabled = toggleEnabled,
            checked = isSwitchedOn,
            onCheckedChange = onToggle
        )
    }
}
