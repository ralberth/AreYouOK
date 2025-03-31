package org.ralberth.areyouok.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuokTopBar(title: String) {
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title)
        }
    )
}


@Composable
fun BottomBarButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(18.dp, 0.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, label)
        }
        Text(
            label,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }

}

@Composable
fun RuokBottomBar(
    onSwitchScreen: (String) -> Unit
//    navActions: RuokNavigationActions
) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomBarButton(
                    "Countdown",
                    Icons.Filled.Refresh,
//                    { navActions.navigateToCountdown() }
                    { onSwitchScreen("countdown") }
                )
                BottomBarButton(
                    "Settings",
                    Icons.Filled.Settings,
//                    { navActions.navigateToSettings() }
                    { onSwitchScreen("settings") }

                )
                BottomBarButton(
                    "Messages",
                    Icons.AutoMirrored.Filled.List,
//                    { navActions.navigateToMessages() }
                    { onSwitchScreen("messages") }
                )
            }
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//fun NavBarsPreview() {
//    AreYouOkTheme {
//        Scaffold(
//            topBar = { RuokTopBar("Preview") },
//            bottomBar = { RuokBottomBar({}, {}, {}) }
//        ) { innerPadding ->
//            Text(
//                "Hithere",
//                modifier = Modifier.padding(innerPadding))
//        }
//    }
//}
