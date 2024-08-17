package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.Orientation
import de.visualdigits.lcars.bufferedimage.type.Style
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.GeneralPath
import java.awt.geom.Rectangle2D

class LCARSImageCorner(
    w: Int,
    h: Int,
    style: Style,
    text: String,
    x: Int = 0,
    y: Int = 0,
    val barWidth: Int = 30,
    val barHeight: Int = 150,
    val roundingFactor: Double = 2.0,
    var barThin: Int = if (barWidth < barHeight) {
        (barWidth * roundingFactor).toInt()
    } else {
        (barHeight * roundingFactor).toInt()
    }
): LCARSImageComponent(w = w, h = h, x = x, y = y, style = style, text = text) {
    
    override fun drawArea(g2d: Graphics2D): Area? {
        return when (style.orientation) {
            Orientation.TOP_LEFT -> createAreaTopLeft()
            Orientation.TOP_RIGHT -> createAreaTopRight()
            Orientation.BOTTOM_LEFT -> createAreaBottomLeft()
            Orientation.BOTTOM_RIGHT -> createAreaBottomRight()
            else -> null
        }
    }

    private fun createAreaTopLeft(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, h.toDouble())
        componentShape.lineTo(0.0, barThin.toDouble())
        componentShape.curveTo(
            0.0, barThin.toDouble(),
            0.0, 0.0,
                barThin.toDouble(), 0.0
        )
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), barHeight.toDouble())
        componentShape.lineTo(barHeight + barThin / 2.0, barHeight.toDouble())
        componentShape.curveTo(
            barHeight + barThin / 2.0, barHeight.toDouble(),
            barHeight.toDouble(), barHeight.toDouble(),
            barHeight.toDouble(), barHeight + barThin / 2.0
        )
        componentShape.lineTo(barHeight.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())

        return Area(componentShape)
    }

    private fun createAreaTopRight(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(w - barThin.toDouble(), 0.0)
        componentShape.curveTo(w - barThin.toDouble(), 0.0, w.toDouble(), 0.0, w.toDouble(), barThin.toDouble())
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(w - barHeight.toDouble(), h.toDouble())
        componentShape.lineTo(w - barHeight.toDouble(), barWidth + barThin / 2.0)
        componentShape.curveTo(w - barHeight.toDouble(), barWidth + barThin / 2.0, w - barHeight.toDouble(), barHeight.toDouble(), w - barHeight - barThin / 2.0, barHeight.toDouble())
        componentShape.lineTo(0.0, barHeight.toDouble())
        componentShape.lineTo(0.0, 0.0)
        componentShape.closePath()

        return Area(componentShape)
    }

    private fun createAreaBottomLeft(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(barHeight.toDouble(), 0.0)
        componentShape.lineTo(barHeight.toDouble(), h - barWidth - barThin / 2.0)
        componentShape.curveTo(barHeight.toDouble(), h - barWidth - barThin / 2.0, barHeight.toDouble(), h - barHeight.toDouble(), barHeight + barThin / 2.0, h - barHeight.toDouble())
        componentShape.lineTo(w.toDouble(), h - barHeight.toDouble())
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(barThin.toDouble(), h.toDouble())
        componentShape.curveTo(barThin.toDouble(), h.toDouble(), 0.0, h.toDouble(), 0.0, h - barThin.toDouble())
        componentShape.closePath()

        return Area(componentShape)
    }

    private fun createAreaBottomRight(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, h - barHeight.toDouble())
        componentShape.lineTo(w - barHeight - barThin / 2.0, h - barHeight.toDouble())
        componentShape.curveTo(w - barHeight - barThin / 2.0, h - barHeight.toDouble(), w - barHeight.toDouble(), h - barHeight.toDouble(), w - barHeight.toDouble(), h - barWidth - barThin / 2.0)
        componentShape.lineTo(w - barHeight.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), h - barThin.toDouble())
        componentShape.curveTo(w.toDouble(), h - barThin.toDouble(), w.toDouble(), h.toDouble(), w - barThin.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())
        componentShape.closePath()

        return Area(componentShape)
    }

    override fun setTextPosition() {
        val fm: FontMetrics = g.fontMetrics
        fm.stringWidth(text)
        val r: Rectangle2D = fm.getStringBounds(text, g)

        when (style.labelPosition) {
            LabelPosition.TOP_LEFT, LabelPosition.TOP_RIGHT -> textY =
                (h - textInsetY)

            LabelPosition.BOTTOM_LEFT, LabelPosition.BOTTOM_RIGHT -> textY =
                (y + r.height).toInt()

            else -> {}
        }

        when (style.labelPosition) {
            LabelPosition.TOP_RIGHT, LabelPosition.BOTTOM_RIGHT -> textX =
                (w - barHeight + textInsetX)

            LabelPosition.TOP_LEFT, LabelPosition.BOTTOM_LEFT -> textX =
                (barHeight - r.width - textInsetX).toInt()

            else -> {}
        }
    }
}
