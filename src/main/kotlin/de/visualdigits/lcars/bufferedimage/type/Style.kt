package de.visualdigits.lcars.bufferedimage.type

import java.awt.Color

data class Style(
    var background: Color? = null,
    var foreground: Color? = Color.white,
    var labelColor: Color? = Color.white,
    var labelPosition: LabelPosition = LabelPosition.CENTER_CENTER,
    var labelSize: Int = 12,
    var labelInsetX: Int = 10,
    var labelInsetY: Int = 10,
    var orientation: Orientation = Orientation.TOP_LEFT,
    var buttonStyle: ButtonStyle = ButtonStyle.RND
)
