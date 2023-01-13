package com.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorForBgEditor(colorBg: String) {

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        val hex = colorBg.replace("#", "")
        val red: Int = hex.substring(0, 2).toInt(16)
        val green: Int = hex.substring(2, 4).toInt(16)
        val blue: Int = hex.substring(4, 6).toInt(16)

        @Stable
        fun getColor(
            red: Int, green: Int, blue: Int, alpha: Int = 0xFF
        ): Color {
            val color = ((alpha and 0xFF) shl 24) or
                    ((red and 0xFF) shl 16) or
                    ((green and 0xFF) shl 8) or
                    (blue and 0xFF)
            return Color(color)
        }

        val newColor = getColor(red, green, blue)

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth(),
            backgroundColor = newColor
        ) {

        }

    }

}