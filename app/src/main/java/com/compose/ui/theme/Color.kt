package com.compose.ui.theme

import androidx.compose.ui.graphics.Color

val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

data class Colors(
    val primaryBackgroundColor: Color,
    val secondaryBackgroundColor: Color,
    val sliderIndicatorColor: Color,
    val sliderTextColor: Color,
    val gradientFirstColor: Color,
    val buttonTextColor: Color,
    val dialogBackgroundColor: Color,
    val textDialogColorHead: Color,
    val textDialogColorStroke: Color,
    val buttonCameraBack: Color
)

val lightPalette = Colors(
    primaryBackgroundColor = Color(0xFFFFFFFF),
    secondaryBackgroundColor = Color(0xFFF2F2F2),
    sliderIndicatorColor = Color(0xFF8F78F3),
    sliderTextColor = Color(0xFFB1B3B4),
    gradientFirstColor = Color(0xFF6E53E8),
    buttonTextColor = Color(0xFF38373A),
    dialogBackgroundColor = Color(0x4D000000),
    textDialogColorHead = Color(0xFF6B6B6B),
    textDialogColorStroke = Color(0xFF979797),
    buttonCameraBack = Color(0x33DADADA)
)
