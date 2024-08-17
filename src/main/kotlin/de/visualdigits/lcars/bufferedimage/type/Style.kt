package de.visualdigits.lcars.bufferedimage.type

import java.awt.Color

data class Style(
    val background: Color = Color.black,
    val foreground: Color? = Color.white,
    val labelPosition: LabelPosition = LabelPosition.CENTER,
    val orientation: Orientation = Orientation.TOP_LEFT,
    val buttonStyle: ButtonStyle = ButtonStyle.RND
)
