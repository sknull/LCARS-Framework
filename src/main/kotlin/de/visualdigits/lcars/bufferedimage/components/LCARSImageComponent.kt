package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.Style
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File

abstract class LCARSImageComponent(
    val w: Int,
    val h: Int,
    val x: Int = 0,
    val y: Int = 0,
    val style: Style,
    val text: String = "",
): BufferedImage(w, h, TYPE_INT_RGB) {

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

    protected val font: Font = Font.createFont(Font.TRUETYPE_FONT, File(ClassLoader.getSystemResource(LabelPosition_FONT_FILENAME).toURI()))

    protected val g = createGraphics()

    protected var textX: Int = 0

    protected var textY: Int = 0

    protected var textInsetX: Int = 0

    protected var textInsetY: Int = 0

    fun draw(): LCARSImageComponent {
        if (isOpaque()) {
            g.color = style.background
            g.fillRect(0, 0, w, h)
        }

        val g2d: Graphics2D = g.create() as Graphics2D

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.color = style.foreground
        g2d.font = font

        drawArea(g2d)?.also { area ->
            g2d.draw(area)
            g2d.fill(area)
        }

        g2d.color = Color.white
        setTextPosition()
        g2d.drawString(text, textX, textY)

        g2d.dispose()

        return this
    }

    abstract fun drawArea(g2d: Graphics2D): Area?


    protected open fun setTextPosition() {
        val fm: FontMetrics = g.fontMetrics
        fm.stringWidth(text)
        val r: Rectangle2D = fm.getStringBounds(text, g)


        when (style.labelPosition) {
            LabelPosition.TOP_LEFT, LabelPosition.TOP, LabelPosition.TOP_RIGHT -> textY =
                (fm.ascent + textInsetY).toInt()

            LabelPosition.BOTTOM_LEFT, LabelPosition.BOTTOM, LabelPosition.BOTTOM_RIGHT -> textY =
                (h - textInsetY).toInt()

            LabelPosition.LEFT, LabelPosition.CENTER, LabelPosition.RIGHT -> textY =
                h / 2 + fm.ascent / 2
        }

        when (style.labelPosition) {
            LabelPosition.TOP_LEFT, LabelPosition.LEFT, LabelPosition.BOTTOM_LEFT -> textX =
                (textInsetX).toInt()

            LabelPosition.TOP_RIGHT, LabelPosition.RIGHT, LabelPosition.BOTTOM_RIGHT -> textX =
                (w - r.width - textInsetX).toInt()

            LabelPosition.TOP, LabelPosition.CENTER, LabelPosition.BOTTOM -> textX =
                (w / 2 - r.width / 2).toInt()
        }
    }

    fun isOpaque(): Boolean = OPAQUE_IMAGE_TYPES.contains(type)
}
