package org.ralberth.areyouok.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuokScaffold(
    navBackCallback: (() -> Unit)? = null,
    content: @Composable() () -> Unit = {}
) {
    var navIcon: @Composable () -> Unit = {}
    if (navBackCallback != null) {
        navIcon = @Composable {
            IconButton(onClick = navBackCallback) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "hithere"
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = navIcon,
                title = {
                    RuokMasthead()
                }
            )
        }
        ) {
            innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxHeight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
        }
}
