package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.CenteredButton


@Composable
fun ContactScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var phoneNameEdit by remember { mutableStateOf(uiState.phoneName) }
    var phoneNumberEdit by remember { mutableStateOf(uiState.phoneNumber) }

    ContactUI(
        navController,
        phoneNameEdit,
        phoneNumberEdit,
        { println("New phone name $it"); phoneNameEdit = it },
        { phoneNumberEdit = it },
        { viewModel.updatePhoneNumber(phoneNameEdit.trim(), phoneNumberEdit.trim()); navController.navigateUp() }
    )
}


@Composable
fun ContactUI(
    navController: NavController?,
    phoneName: String,
    phoneNumber: String,
    onUpdateName: (String) -> Unit,
    onUpdateNumber: (String) -> Unit,
    onDone: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "contact",
        title = "Point of Contact",
        description = "Person who should receive text messages if you are unresponsive.  Enter " +
                "in any name and a valid phone number.  To select from your phone's contacts, " +
                "enable \"Permission to read contacts\" on the permissions screen (padlock icon " +
                "on the bottom nav bar).",
        onNavigateUp = onDone
    ) {
        OutlinedTextField(
            value = phoneName,
            onValueChange = onUpdateName,
            singleLine = true,
            label = { Text("Contact name") },
            leadingIcon = {
                Icon(Icons.Default.Face, contentDescription = "Face")
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onUpdateNumber,
            singleLine = true,
            label = { Text("Phone number") },
            leadingIcon = {
                Icon(Icons.Default.Call, contentDescription = "Phone")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Next
            )
        )

        CenteredButton(
            onClick = onDone,
            modifier = Modifier.padding(20.dp)
        )
    }
}


@PreviewLightDark
@Composable
fun ContactUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ContactUI(
                null,
                "John Smith",
                "",
                {},
                {},
                {}
            )
        }
    }
}
