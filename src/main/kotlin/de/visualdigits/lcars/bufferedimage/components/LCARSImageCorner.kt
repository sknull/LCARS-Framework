package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.Orientation
import de.visualdigits.lcars.bufferedimage.type.Style
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.GeneralPath
import kotlin.math.roundToInt

class LCARSImageCorner(
    w: Int,
    h: Int,
    style: Style,
    text: String,
    x: Int = 0,
    y: Int = 0,
    val barV: Int = 30,
    val barH: Int = 150,
    val roundingFactor: Double = 2.0,
): LCARSImageComponent<LCARSImageCorner>(w, h, x, y, style, text) {

    private val barThin: Double

    init {
        if (barV < barH) {
            barThin = (barV * roundingFactor)
        } else {
            barThin = (barH * roundingFactor)
        }
    }

    override fun determineLabelSize(): Int {
        return barV - 4
    }

    override fun determineLabelPosition(labelInsetX: Int, labelInsetY: Int): Pair<Int, Int>? {
        val fontMetrics: FontMetrics = g.fontMetrics
        fontMetrics.descent
        val bounds = fontMetrics.getStringBounds(label, g)

        val y = when (style.orientation) {
            Orientation.TOP_LEFT -> when (style.labelPosition) {
                LabelPosition.TOP_LEFT, LabelPosition.TOP_CENTER, LabelPosition.TOP_RIGHT ->
                    ((barV - bounds.height) / 2.0 - bounds.y).roundToInt()
                LabelPosition.BOTTOM_LEFT ->
                    (h - labelInsetY - bounds.height - bounds.y).roundToInt()
                LabelPosition.CENTER_LEFT ->
                    ((h - bounds.height) / 2.0 - bounds.y).roundToInt()
                else -> null
            }
            Orientation.TOP_RIGHT -> when (style.labelPosition) {
                LabelPosition.TOP_LEFT, LabelPosition.TOP_CENTER, LabelPosition.TOP_RIGHT ->
                    ((barV - bounds.height) / 2.0 - bounds.y).roundToInt()
                LabelPosition.BOTTOM_RIGHT ->
                    (h - labelInsetY - bounds.height - bounds.y).roundToInt()
                LabelPosition.CENTER_RIGHT ->
                    ((h - bounds.height) / 2.0 - bounds.y).roundToInt()
                else -> null
            }
            Orientation.BOTTOM_LEFT -> when (style.labelPosition) {
                LabelPosition.BOTTOM_LEFT, LabelPosition.BOTTOM_CENTER, LabelPosition.BOTTOM_RIGHT ->
                    (h - barV + (barV - bounds.height) / 2.0 - bounds.y).roundToInt()
                LabelPosition.TOP_LEFT ->
                    (labelInsetY - bounds.y).roundToInt()
                LabelPosition.CENTER_LEFT ->
                    ((h - bounds.height) / 2.0 - bounds.y).roundToInt()
                else -> null
            }
            Orientation.BOTTOM_RIGHT -> when (style.labelPosition) {
                LabelPosition.BOTTOM_LEFT, LabelPosition.BOTTOM_CENTER, LabelPosition.BOTTOM_RIGHT ->
                    (h - barV + (barV - bounds.height) / 2.0 - bounds.y).roundToInt()
                LabelPosition.TOP_RIGHT ->
                    (labelInsetY - bounds.y).roundToInt()
                LabelPosition.CENTER_RIGHT ->
                    ((h - bounds.height) / 2.0 - bounds.y).roundToInt()
                else -> null
            }
            else -> null
        }

        val x = when (style.orientation) {
            Orientation.TOP_LEFT -> when (style.labelPosition) {
                LabelPosition.TOP_LEFT, LabelPosition.CENTER_LEFT, LabelPosition.BOTTOM_LEFT ->
                    ((barH - bounds.width) / 2.0).roundToInt()
                LabelPosition.TOP_CENTER ->
                    ((w - bounds.width) / 2.0).roundToInt()
                LabelPosition.TOP_RIGHT ->
                    (w - bounds.width - labelInsetX).roundToInt()
                else -> null
            }
            Orientation.TOP_RIGHT -> when (style.labelPosition) {
                LabelPosition.TOP_RIGHT, LabelPosition.CENTER_RIGHT, LabelPosition.BOTTOM_RIGHT ->
                    (w - barH + (barH - bounds.width) / 2.0).roundToInt()
                LabelPosition.TOP_CENTER ->
                    ((w - bounds.width) / 2.0).roundToInt()
                LabelPosition.TOP_LEFT ->
                    labelInsetX
                else -> null
            }
            Orientation.BOTTOM_LEFT-> when (style.labelPosition) {
                LabelPosition.TOP_LEFT, LabelPosition.CENTER_LEFT, LabelPosition.BOTTOM_LEFT ->
                    ((barH - bounds.width) / 2.0).roundToInt()
                LabelPosition.BOTTOM_CENTER ->
                    ((w - bounds.width) / 2.0).roundToInt()
                LabelPosition.BOTTOM_RIGHT ->
                    (w - bounds.width - labelInsetX).roundToInt()
                else -> null
            }
            Orientation.BOTTOM_RIGHT -> when (style.labelPosition) {
                LabelPosition.TOP_RIGHT, LabelPosition.CENTER_RIGHT, LabelPosition.BOTTOM_RIGHT ->
                    (w - barH + (barH - bounds.width) / 2.0).roundToInt()
                LabelPosition.BOTTOM_CENTER ->
                    ((w - bounds.width) / 2.0).roundToInt()
                LabelPosition.BOTTOM_LEFT ->
                    labelInsetX
                else -> null
            }
            else -> null
        }

        return x?.let { xx -> y?.let { yy -> Pair(xx, yy) } }
    }

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
        componentShape.lineTo(0.0, barThin)
        componentShape.curveTo(0.0, barThin, 0.0, 0.0, barThin, 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), barV.toDouble())
        componentShape.lineTo(barH + barThin / 2, barV.toDouble())
        componentShape.curveTo(barH + barThin / 2.0, barV.toDouble(), barH.toDouble(), barV.toDouble(), barH.toDouble(), barV + barThin / 2.0)
        componentShape.lineTo(barH.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())

        return Area(componentShape)
    }

    private fun createAreaTopRight(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(w - barThin, 0.0)
        componentShape.curveTo(w - barThin, 0.0, w.toDouble(), 0.0, w.toDouble(), barThin)
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(w - barH.toDouble(), h.toDouble())
        componentShape.lineTo(w - barH.toDouble(), barV + barThin / 2.0)
        componentShape.curveTo(w - barH.toDouble(), barV + barThin / 2.0, w - barH.toDouble(), barV.toDouble(), w - barH - barThin / 2.0, barV.toDouble())
        componentShape.lineTo(0.0, barV.toDouble())
        componentShape.lineTo(0.0, 0.0)
        componentShape.closePath()

        return Area(componentShape)
    }

    private fun createAreaBottomLeft(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(barH.toDouble(), 0.0)
        componentShape.lineTo(barH.toDouble(), h - barV - barThin / 2.0)
        componentShape.curveTo(barH.toDouble(), h - barV - barThin / 2.0, barH.toDouble(), h - barV.toDouble(), barH + barThin / 2.0, h - barV.toDouble())
        componentShape.lineTo(w.toDouble(), h - barV.toDouble())
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(barThin, h.toDouble())
        componentShape.curveTo(barThin, h.toDouble(), 0.0, h.toDouble(), 0.0, h - barThin)
        componentShape.closePath()

        return Area(componentShape)
    }

    private fun createAreaBottomRight(): Area {
        val componentShape = GeneralPath()

        componentShape.moveTo(0.0, h - barV.toDouble())
        componentShape.lineTo(w - barH - barThin / 2.0, h - barV.toDouble())
        componentShape.curveTo(w - barH - barThin / 2.0, h - barV.toDouble(), w - barH.toDouble(), h - barV.toDouble(), w - barH.toDouble(), h - barV - barThin / 2.0)
        componentShape.lineTo(w - barH.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), h - barThin)
        componentShape.curveTo(w.toDouble(), h - barThin, w.toDouble(), h.toDouble(), w - barThin, h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())
        componentShape.closePath()

        return Area(componentShape)
    }
}

