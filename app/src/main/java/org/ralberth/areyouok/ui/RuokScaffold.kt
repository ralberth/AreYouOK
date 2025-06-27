package org.ralberth.areyouok.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.theme.AreYouOkTheme


@Composable
fun BottomNavButton(navController: NavController?, myRoute: String, navRoute: String, icon: ImageVector) {
    IconButton(
        onClick = { if (myRoute != navRoute) navController?.navigate(navRoute) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = navRoute
        )
    }
}


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuokScaffold(
    navController: NavController?, // always pass one.  This is optional for @Preview screens
    route: String,
    title: String? = null,
    description: String? = null,
    showNavigateUp: Boolean = true,
    onNavigateUp: () -> Unit = {},
    content: @Composable() () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    RuokMasthead()
                },
                navigationIcon = {
                    if (showNavigateUp)
                        IconButton(onClick = { onNavigateUp(); navController?.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to last screen"
                            )
                        }
                },
//                actions = {
//                    if (showSettings)
//                        IconButton(onClick = { navController?.navigate("settings") }) {
//                            Icon(Icons.Outlined.Settings, "Settings")
//                        }
//                }
            )
        },

        bottomBar = {
            BottomAppBar {
                Spacer(Modifier.weight(1f))
                BottomNavButton(
                    navController = navController,
                    myRoute = route,
                    navRoute = "main",
                    icon = Icons.Filled.Home
                )
                Spacer(Modifier.weight(2f))
                BottomNavButton(
                    navController = navController,
                    myRoute = route,
                    navRoute = "countdown",
                    icon = Icons.Filled.Notifications
                )
                Spacer(Modifier.weight(2f))
                BottomNavButton(
                    navController = navController,
                    myRoute = route,
                    navRoute = "settings",
                    icon = Icons.Filled.Settings
                )
                Spacer(Modifier.weight(2f))
                BottomNavButton(
                    navController = navController,
                    myRoute = route,
                    navRoute = "permissions",
                    icon = Icons.Filled.Lock
                )
                Spacer(Modifier.weight(1f))
            }
        }
    ) {
        innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
                if (title != null)
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .fillMaxWidth()
                            .padding(5.dp)

                    )

                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(18.dp)
                    )
                    HorizontalDivider()
                }

                content()
            }
    }
}


@PreviewLightDark
@Composable
fun RuokScaffoldPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RuokScaffold(
                null,
                "a",
                "ScaffoldPreview",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas metus sem, " +
                        "lacinia sit amet suscipit eget, malesuada quis lacus. Vestibulum pellentesque, " +
                        "est vitae facilisis fermentum, metus sapien interdum tellus, nec tempor sem " +
                        "dolor a tellus. "
            ) {
                Text("Hi There")
            }
        }
    }
}
