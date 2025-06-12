package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun PhoneNumber(enabled: Boolean, name: String, number: String, onChangeButtonPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Phone number", fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))

        when {
            name.length == 0 && number.length == 0 -> Text("Pick a contact")
            name.length == 0 -> Text(number)   // number.length guaranteed > 0 at this point
            else -> Column { Text(name); Text(number) }
        }

        TextButton(
            enabled = enabled,
            onClick = onChangeButtonPressed
        ) {
            Icon(Icons.Filled.Edit, "Change")
        }
    }
}
