package org.ralberth.areyouok.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp


val RStyle = SpanStyle(
    color = Color(250, 0, 255),
    fontSize = 50.sp,
    fontFamily =  FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
    baselineShift = BaselineShift(-0.2f),
    shadow = Shadow(color = Color.DarkGray, offset = Offset(7f, 7f), blurRadius = 7f)
)

val UStyle = SpanStyle(
    color = Color(0, 243, 212),
    fontSize = 50.sp,
    fontFamily = FontFamily.Serif,
    fontStyle = FontStyle.Italic,
    fontWeight = FontWeight.Medium,
    shadow = Shadow(color = Color.DarkGray, offset = Offset(7f, 7f), blurRadius = 7f)
)

val OStyle = SpanStyle(
    color = Color(255, 240, 211),
    fontSize = 50.sp,
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.SemiBold,
    baselineShift = BaselineShift(-0.25f),
    shadow = Shadow(color = Color.DarkGray, offset = Offset(7f, 7f), blurRadius = 7f)

)

val KStyle = SpanStyle(
    color = Color(204, 194, 255),
    fontSize = 45.sp,
    fontFamily = FontFamily.Cursive,
    fontWeight = FontWeight.Bold,
    shadow = Shadow(color = Color.DarkGray, offset = Offset(7f, 7f), blurRadius = 7f)
)

val QStyle = SpanStyle(
    color = Color(164, 248, 35),
    fontSize = 50.sp,
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Normal,
    shadow = Shadow(color = Color.DarkGray, offset = Offset(7f, 7f), blurRadius = 7f)
)


@Composable
fun RuokMasthead() {
    Text(
        buildAnnotatedString {
            withStyle(style = RStyle) { append("R") }
            withStyle(style = UStyle) { append("U") }
            withStyle(style = OStyle) { append("O") }
            withStyle(style = KStyle) { append("K") }
            withStyle(style = QStyle) { append("?") }
        }
    )
}
