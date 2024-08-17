package de.visualdigits.lcars.awt.elements

import de.visualdigits.lcars.awt.LCARS
import org.slf4j.LoggerFactory
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.geom.Area
import java.awt.geom.GeneralPath
import java.awt.geom.Rectangle2D

/**
 * The default constructor for the LCARSCorner class. Uses default values for a number
 * of parameters to create a shape very close to LCARS canon. Only location and width
 * are variable. There are canonical values for height and the thickness of the vertical
 * and horizontal bars.
 *
 * @param x
 * @param y
 * @param w
 * @param barH
 * @param parV
 * @param style
 */
class LCARSCorner(
    x: Int,
    y: Int,
    w: Int,
    h: Int = 90,
    var barH: Int = 30,
    var barV: Int = 150,
    style: Int
) : LCARSComponent(componentText = "", x = x, y = y, w = w, h = h, style = style) {

    private val LOGGER: org.slf4j.Logger = LoggerFactory.getLogger(LCARSCorner::class.java)

    private var barThin: Int = 0

    /**
     * Set the `roundingFactor` to 2.0 for the outer and inner rounded corners.
     * This will give the corner a close to LCARS canonical shape.
     */
    private var roundingFactor: Double = 2.0


    init {
        /**
         * Initialize the component with the LCARS canonical vertical and horizontal bar
         * thicknesses, and the component's style.
         */
        init(style)
    }

    private fun init(style: Int) {
        barThin = if (barH < barV) {
            (barH * roundingFactor).toInt()
        } else {
            (barV * roundingFactor).toInt()
        }

        /**
         * Mask the LCARS style with `ES_SHAPE` to extract the corner shape.
         */
        val shape: Int = LCARS.getShape(style)

        /**
         * Create the component shape.
         */
        when (shape) {
            LCARS.ES_SHAPE_NE -> createNEShape()
            LCARS.ES_SHAPE_NW -> createNWShape()
            LCARS.ES_SHAPE_SE -> createSEShape()
            LCARS.ES_SHAPE_SW -> createSWShape()
            else ->
                /**
                 * Log an error if there was no shape specified or an invalid shape specified.
                 */
                LOGGER.error("No LCARS corner shape selected.")
        }
    }

    /**
     * Create the shape for a North West or upper left LCARS corner. Uses a
     * GeneralPath object to construct the shape. Then creates an Area object
     * from the GeneralPath, assigns it to the `area` class variable,
     * and returns the GeneralPath object.
     *
     * @return the GeneralPath object that defines the shape of the LCARS corner
     */
    private fun createNWShape(): GeneralPath {
        /**
         * Create a new GeneralPath object.
         */

        val componentShape: GeneralPath = GeneralPath()

        /**
         * Build the component shape using the GeneralPath object. It is made up of
         * six lines and an inner and outer curve.
         */
        componentShape.moveTo(0.0, h.toDouble())
        componentShape.lineTo(0.0, barThin.toDouble())
        componentShape.curveTo(0.0, barThin.toDouble(), 0.0, 0.0, barThin.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), barH.toDouble())
        componentShape.lineTo(barV + barThin.toDouble() / 2, barH.toDouble())
        componentShape.curveTo(barV + barThin / 2.0, barH.toDouble(), barV.toDouble(), barH.toDouble(), barV.toDouble(), barH + barThin / 2.0)
        componentShape.lineTo(barV.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())

        /**
         * Create a new Area object using the GeneralPath, and assign it to the object's
         * `area` variable.
         */
        area = Area(componentShape)

        /**
         * Return the GeneralPath in case someone wants to use it.
         */
        return componentShape
    }

    private fun createNEShape(): GeneralPath {
        val componentShape: GeneralPath = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(w - barThin.toDouble(), 0.0)
        componentShape.curveTo(w - barThin.toDouble(), 0.0, w.toDouble(), 0.0, w.toDouble(), barThin.toDouble())
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(w - barV.toDouble(), h.toDouble())
        componentShape.lineTo(w - barV.toDouble(), barH + barThin / 2.0)
        componentShape.curveTo(w - barV.toDouble(), barH + barThin / 2.0, w - barV.toDouble(), barH.toDouble(), w - barV - barThin / 2.0, barH.toDouble())
        componentShape.lineTo(0.0, barH.toDouble())
        componentShape.lineTo(0.0, 0.0)
        componentShape.closePath()

        area = Area(componentShape)

        return componentShape
    }

    private fun createSWShape(): GeneralPath {
        val componentShape: GeneralPath = GeneralPath()

        componentShape.moveTo(0.0, 0.0)
        componentShape.lineTo(barV.toDouble(), 0.0)
        componentShape.lineTo(barV.toDouble(), h - barH - barThin / 2.0)
        componentShape.curveTo(barV.toDouble(), h - barH - barThin / 2.0, barV.toDouble(), h - barH.toDouble(), barV + barThin / 2.0, h - barH.toDouble())
        componentShape.lineTo(w.toDouble(), h - barH.toDouble())
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(barThin.toDouble(), h.toDouble())
        componentShape.curveTo(barThin.toDouble(), h.toDouble(), 0.0, h.toDouble(), 0.0, h - barThin.toDouble())
        componentShape.closePath()

        area = Area(componentShape)

        return componentShape
    }

    private fun createSEShape(): GeneralPath {
        val componentShape: GeneralPath = GeneralPath()

        componentShape.moveTo(0.0, h - barH.toDouble())
        componentShape.lineTo(w - barV - barThin / 2.0, h - barH.toDouble())
        componentShape.curveTo(w - barV - barThin / 2.0, h - barH.toDouble(), w - barV.toDouble(), h - barH.toDouble(), w - barV.toDouble(), h - barH - barThin / 2.0)
        componentShape.lineTo(w - barV.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), h - barThin.toDouble())
        componentShape.curveTo(w.toDouble(), h - barThin.toDouble(), w.toDouble(), h.toDouble(), w - barThin.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())
        componentShape.closePath()

        area = Area(componentShape)

        return componentShape
    }

    override fun setTextPosition(g2d: Graphics2D) {
        /**
         * Get the font metrics and the bounding rectangle for the component's text
         * for use calculating the text position.
         */
        val fm: FontMetrics = g2d.fontMetrics
        fm.stringWidth(componentText)
        val r: Rectangle2D = fm.getStringBounds(componentText, g2d)

        /**
         * Mask the LCARS style with `ES_SHAPE` to extract the corner shape.
         */
        val shape: Int = LCARS.getShape(style)

        when (shape) {
            LCARS.ES_SHAPE_NE, LCARS.ES_SHAPE_NW -> textY =
                (scaledArea!!.bounds.height - textInsetY * scaleFactor).toInt()

            LCARS.ES_SHAPE_SE, LCARS.ES_SHAPE_SW -> textY =
                (scaledArea!!.bounds.y + r.height).toInt()

            else -> {}
        }

        when (shape) {
            LCARS.ES_SHAPE_NE, LCARS.ES_SHAPE_SE -> textX =
                (scaledArea!!.bounds.width - barV * scaleFactor + textInsetX * scaleFactor).toInt()

            LCARS.ES_SHAPE_NW, LCARS.ES_SHAPE_SW -> textX =
                (barV * scaleFactor - r.width - textInsetX * scaleFactor).toInt()

            else -> {}
        }
    }
}
