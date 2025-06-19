package org.ralberth.areyouok.ui.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.datamodel.RuokViewModel
import java.time.Instant
import kotlin.text.*


@Composable
fun RowScope.TableCell(
    thing: Any,
    weight: Float = 1.0f
) {
    when {
        thing is String ->  Text(
                                text = thing,
                                Modifier
//                                    .border(1.dp, Color.Black)
                                    .weight(weight)
                                    .padding(4.dp)
                            )
        thing is ImageVector -> Icon(
                                    thing,
                    "hi",
                                    modifier = Modifier.padding(4.dp).weight(weight)
                                )
        else -> thing
    }
}


class TableBuilder() {
    val columnWeights: ArrayList<Int> = arrayListOf()
    val headerRow: ArrayList<String> = arrayListOf()
    val rows: ArrayList<ArrayList<Any>> = arrayListOf()

    fun columnWeights(vararg numbers: Int): TableBuilder {
        for(num in numbers)
            columnWeights.add(num)
        return this
    }

    fun headerRow(vararg cells: String): TableBuilder {
        for (cell in cells)
            headerRow.add(cell)
        return this
    }


    fun row(vararg cells: Any): TableBuilder {
        val row: ArrayList<Any> = arrayListOf()
        for (cell in cells)
            row.add(cell)
        rows.add(row)
        return this
    }

    @Composable
    fun build() {
        Column(Modifier
            .fillMaxSize()
            .padding(20.dp)
        ) {
            if (headerRow.size >= 1) {
//                item {
                    Row {
                        for (i in 0..headerRow.size - 1)
                            TableCell(thing = headerRow[i], weight = columnWeights[i].toFloat())
                    }
//                }
            }
            for (row in rows) {
//                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        var i: Int = 0
                        for (cell in row) {
                            TableCell(thing = cell, weight = columnWeights[i].toFloat())
                            i += 1
                        }
                    }
//                }
            }
        }
    }
}
