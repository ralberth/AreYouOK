package org.ralberth.areyouok.ui.settings

import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.R
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.permissions.PermissionsHelper
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.ErrorStripe


@Composable
fun ForegroundScreen(navController: NavController, permHelper: PermissionsHelper, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ForegroundUI(
        navController,
        Settings.canDrawOverlays(navController.context),
        uiState.foregroundOnAlerts,
        viewModel::updateForegroundOnAlerts
    )
}


@Composable
fun ForegroundUI(
    navController: NavController?,
    hasForegroundPermission: Boolean,
    foregroundValue: Boolean,
    onNewForegroundValue: (Boolean) -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "foreground",
        title = "App Foreground Setting",
        description = "When the app counts-down to 1-minute left and no minutes left, should " +
                "the app come to the foreground, interrupting anything you're doing?"
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            Text(
                "T-3 Minutes, T-2 Minutes",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                "At 3 minutes before and 2-minutes before the countdown ends, the app always " +
                        "displays a notification banner only."
            )
            TwoImageRow( R.drawable.banner_3min, R.drawable.banner_2min)

            Text(
                "T-1 Minute, Countdown Ended",
                style = MaterialTheme.typography.headlineSmall
            )
            Text("Notification banners are always displayed for all alerts, including these.")
            TwoImageRow( R.drawable.banner_1min, R.drawable.banner_0min)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Also bring the app to the foreground")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    enabled = hasForegroundPermission,
                    checked = foregroundValue,
                    onCheckedChange = onNewForegroundValue
                )
            }
            ErrorStripe(
                shouldDisplay = !hasForegroundPermission,
                message = "Can't enable this until you enable Foreground permission."
            )
        }
    }
}


@Composable
fun TwoImageRow(
    image1: Int, // Resource
    image2: Int  // Resource
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 40.dp, start = 30.dp, end = 30.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = image1),
            contentDescription = "notification banner example",
            contentScale = ContentScale.Fit,
            modifier = Modifier.border(1.dp, Color.Black)
        )
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = image2),
            contentDescription = "notification banner example",
            contentScale = ContentScale.Fit,
            modifier = Modifier.border(1.dp, Color.Black)
        )
        Spacer(Modifier.weight(1f))
    }
}


@PreviewLightDark
@Composable
fun ForegroundUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ForegroundUI(
                null,
                false,
                true,
                {}
            )
        }
    }
}
