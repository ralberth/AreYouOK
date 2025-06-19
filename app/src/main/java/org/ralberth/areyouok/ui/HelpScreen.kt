package org.ralberth.areyouok.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import org.ralberth.areyouok.R
import org.ralberth.areyouok.ui.utils.Markdown
import java.io.InputStream


@Composable
fun HelpScreen(navController: NavController?) {
    RuokScaffold(
        navController = navController,
        route = "help",
        title = "Help"
    ) {
        val stream: InputStream = LocalContext.current.resources.openRawResource(R.raw.helptext)
        val text = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        Markdown(text)
    }
}


@PreviewLightDark
@Composable
fun HelpScreenPreview() {
    HelpScreen(null)
}
