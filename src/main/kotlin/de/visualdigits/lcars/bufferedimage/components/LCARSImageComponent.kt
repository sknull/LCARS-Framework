package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.Style
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Area
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.roundToInt

@Suppress("UNCHECKED_CAST")
abstract class LCARSImageComponent<T : LCARSImageComponent<T>>(
    val w: Int,
    val h: Int,
    val x: Int = 0,
    val y: Int = 0,
    val style: Style,
    val label: String = "",
): BufferedImage(w, h, style.background?.let { TYPE_INT_RGB }?:TYPE_INT_ARGB ) {

    companion object {

        val LabelPosition_FONT_FILENAME = "swz911uc.ttf"

        val OPAQUE_IMAGE_TYPES = setOf(
            TYPE_INT_RGB,
            TYPE_INT_BGR,
            TYPE_3BYTE_BGR,
            TYPE_USHORT_565_RGB,
            TYPE_USHORT_555_RGB,
            TYPE_BYTE_GRAY,
            TYPE_USHORT_GRAY,
            TYPE_BYTE_BINARY,
            TYPE_BYTE_INDEXED
        )
    }

    protected val g = graphics as Graphics2D

    protected val font: Font = Font.createFont(Font.TRUETYPE_FONT, File(ClassLoader.getSystemResource(LabelPosition_FONT_FILENAME).toURI()))

    fun draw(): T {
        if (isOpaque()) {
            g.color = style.background
            g.fillRect(0, 0, w, h)
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

//        drawCross()

        g.color = style.foreground

        drawArea(g)?.also { area ->
            g.composite = AlphaComposite.SrcOver
            g.draw(area)
            g.fill(area)
        }

        val labelSize = determineLabelSize()
        val f = g.font
        g.font = Font(font.name, 0, labelSize)
        determineLabelPosition(style.labelInsetX, style.labelInsetY)?.also { position -> drawLabel(position) }
        g.font = f

        g.dispose()

        return this as T
    }

    private fun drawCross() {
        g.color = Color.red
        g.drawRect(0, 0, w - 1, h - 1)
        g.drawLine((w / 2.0 - 1).roundToInt(), 0, (w / 2.0 - 1).roundToInt(), h)
        g.drawLine(0, (h / 2.0 - 1).roundToInt(), w, (h / 2.0 - 1).roundToInt())
        g.drawLine(0, 0, w, h)
        g.drawLine(0, w, 0, h)
    }

    fun drawLabel(position: Pair<Int, Int>) {
        val c = g.color
        g.color = style.labelColor
        g.drawString(label, position.first, position.second)
        g.color = c
    }

    abstract fun drawArea(g2d: Graphics2D): Area?

    protected open fun determineLabelSize(): Int {
        return style.labelSize
    }

    protected open fun determineLabelInsets(labelSize: Int): Pair<Int, Int> {
        return Pair(10, 10)
    }

    protected open fun determineLabelPosition(labelInsetX: Int, labelInsetY: Int): Pair<Int, Int>? {
        val fontMetrics: FontMetrics = g.fontMetrics
        val bounds = fontMetrics.getStringBounds(label, g)

        val y = when (style.labelPosition) {
            LabelPosition.TOP_LEFT, LabelPosition.TOP_CENTER, LabelPosition.TOP_RIGHT ->
                (labelInsetY + bounds.height + bounds.y / 2.0).roundToInt()
            LabelPosition.BOTTOM_LEFT, LabelPosition.BOTTOM_CENTER, LabelPosition.BOTTOM_RIGHT ->
                (h - labelInsetY - bounds.height - bounds.y).roundToInt()
            LabelPosition.CENTER_LEFT, LabelPosition.CENTER_CENTER, LabelPosition.CENTER_RIGHT ->
                ((h - bounds.height) / 2.0 - bounds.y).roundToInt()
        }

        val x = when (style.labelPosition) {
            LabelPosition.TOP_LEFT, LabelPosition.CENTER_LEFT, LabelPosition.BOTTOM_LEFT ->
                labelInsetX
            LabelPosition.TOP_RIGHT, LabelPosition.CENTER_RIGHT, LabelPosition.BOTTOM_RIGHT ->
                (w - labelInsetX - bounds.width).roundToInt()
            LabelPosition.TOP_CENTER, LabelPosition.CENTER_CENTER, LabelPosition.BOTTOM_CENTER ->
                ((w - bounds.width) / 2.0).roundToInt()
        }

        return Pair(x, y)
    }

    fun isOpaque(): Boolean = OPAQUE_IMAGE_TYPES.contains(type)
}
