package org.ralberth.areyouok.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Markdown(text: String) {
    Column(
        modifier = Modifier.padding(start = 18.dp, end= 18.dp, top = 4.dp, bottom = 4.dp)
    ) {
        val rows = text.split('\n')
        for (row in rows) {
            if (!row.isBlank()) {
                when {
                    row.startsWith("#") -> MarkdownHeading(row)
                    row.startsWith("*") -> MarkdownBullet(row.substring(2))
                    else -> MarkdownBody(row)
                }
            }
        }
    }
}


@Composable
fun MarkdownHeading(title: String) {
    val rowParts = title.split(' ', limit =  2)
    val level = rowParts[0].length
    val fontSize = (26 - (level * 2)).sp
    val fontStyle = if (level % 2 == 0) FontStyle.Normal else FontStyle.Italic
    val textAlign = if (level == 1) TextAlign.Center else TextAlign.Start

    Text(
        text = rowParts[1],
        fontSize = fontSize,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontStyle = fontStyle,
        textAlign = textAlign,
        modifier = Modifier.padding(top = 18.dp).fillMaxWidth()
    )
}


@Composable
fun MarkdownBody(body: String) {
    Text(
        text = body,
        fontFamily = FontFamily.Serif,
        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
    )
}


@Composable
fun MarkdownBullet(bullet: String) {
    Row(
        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
    ) {
        Column { Text("   🔸 ") }
        Column { Text(bullet) }
    }
}


@Preview(showBackground = true)
@Composable
fun MarkdownPreview() {
    Markdown(
        """
        # Big Title
        # Subtitle
        
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas metus sem, lacinia sit amet suscipit eget, malesuada quis lacus.
        Praesent vel ex non nibh scelerisque hendrerit et nec lorem. Vivamus libero ante, sagittis non facilisis sed.
        
        ## Heading 1
        Vivamus tortor mauris, tempus ac dolor vitae, lobortis posuere mauris.

        ### Heading 2
        Phasellus ultrices elementum massa, et mattis mi tempor sit amet. Vivamus sodales pulvinar ligula elementum faucibus. Proin ornare massa purus, luctus egestas lacus maximus eget.

        #### Heading 3
        
        * Mauris mattis porta varius
        * Pellentesque habitant morbi tristique
        * Ssenectus et netus et malesuada fames ac turpis egestas. Vivamus rhoncus est id ultricies suscipit. Nulla scelerisque elit aliquet imperdiet euismod.
         
        Phasellus dapibus facilisis nibh laoreet tempor. Fusce et metus quam. Sed ut mi dignissim, maximus justo vel, fermentum dolor. Etiam elit ipsum, pharetra eu eros volutpat, semper suscipit metus. Sed lacinia odio a libero gravida viverra eget id sapien. Fusce auctor mauris est, a pretium felis ullamcorper et. Nunc ac mi in mauris ultricies iaculis. Mauris sollicitudin mi ac elit malesuada venenatis. Praesent eu libero vel dui blandit vulputate. Suspendisse viverra ligula vitae rhoncus tempus.
        
        Vestibulum elementum magna quis nisl congue consequat. In commodo, sem eget mattis semper, metus mi tristique libero, vitae luctus nulla quam non ex. Proin vel venenatis risus. Cras tristique velit ut lectus fermentum, vel egestas sem sagittis. Vestibulum vitae diam quam. Suspendisse in ligula efficitur, tincidunt ligula id, rhoncus purus. Phasellus aliquet fermentum est, id eleifend lacus laoreet ut. Suspendisse volutpat velit eget consectetur accumsan. In pharetra nisl vel auctor pellentesque. Curabitur ac nibh at dui elementum hendrerit in id est. Maecenas suscipit risus vitae eros mollis, vitae tempus ipsum ultricies. Proin dapibus, sapien at gravida sagittis, orci sem suscipit quam, ac venenatis elit neque eu est. Quisque orci dolor, scelerisque eget ultricies eget, mollis pretium arcu. Vivamus scelerisque magna vulputate, dignissim metus vel, efficitur augue. Donec ut massa vitae turpis commodo lacinia sed ut nulla. In feugiat, erat a gravida convallis, leo lorem commodo enim, ut dictum dolor sapien sed tortor.
        
        Sed sodales vulputate lacus, nec ultrices eros pulvinar a. Praesent tempus aliquam risus non molestie. Nullam tincidunt augue aliquet purus convallis hendrerit at vitae nisl. Cras nulla neque, dignissim eu porttitor id, interdum vitae nisi. Nulla hendrerit vulputate ipsum ac ultrices. Suspendisse potenti. Suspendisse eget sem quis ante euismod tempor non non massa. Proin condimentum, augue vel tempor dignissim, nulla velit euismod nunc, a sollicitudin arcu diam quis nisl. Praesent et rhoncus augue. Pellentesque fermentum ex quis velit iaculis lobortis. 
    """.trimIndent())
}
