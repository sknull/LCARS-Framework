package de.visualdigits.lcars.elements

import de.visualdigits.lcars.LCARS
import java.awt.geom.Area
import java.awt.geom.GeneralPath

open class LCARSRectangle(x: Int, y: Int, w: Int, h: Int, style: Int) :
    LCARSComponent("", x, y, w, h, style) {
    init {
        // TODO Auto-generated constructor stub
        createRectangleShape()
    }

    private fun createRectangleShape(): GeneralPath {
        /**
         * Create a new GeneralPath object.
         */
        val componentShape: GeneralPath = GeneralPath()

        /**
         * Build a simple rectangle shape using the `GeneralPath`
         * based on the dimension arguments and the style.
         */
        when (LCARS.getRectangleShape(style)) {
            LCARS.ES_RECT_RND -> {
                componentShape.moveTo(h / 2.0, 0.0)
                componentShape.lineTo(w - h / 2.0, 0.0)
                componentShape.curveTo(w - h / 2.0, 0.0, w.toDouble(), 0.0, w.toDouble(), h / 2.0)
                componentShape.curveTo(w.toDouble(), h / 2.0, w.toDouble(), h.toDouble(), w - h / 2.0, h.toDouble())
                componentShape.lineTo(h / 2.0, h.toDouble())
                componentShape.curveTo(h / 2.0, h.toDouble(), 0.0, h.toDouble(), 0.0, h / 2.0)
                componentShape.curveTo(0.0, h / 2.0, 0.0, 0.0, h / 2.0, 0.0)
            }

            LCARS.ES_RECT_RND_E -> {
                componentShape.moveTo(0.0, 0.0)
                componentShape.lineTo(w - h / 2.0, 0.0)
                componentShape.curveTo(w - h / 2.0, 0.0, w.toDouble(), 0.0, w.toDouble(), h / 2.0)
                componentShape.curveTo(w.toDouble(), h / 2.0, w.toDouble(), h.toDouble(), w - h / 2.0, h.toDouble())
                componentShape.lineTo(0.0, h.toDouble())
                componentShape.lineTo(0.0, 0.0)
            }

            LCARS.ES_RECT_RND_W -> {
                componentShape.moveTo(h / 2.0, 0.0)
                componentShape.lineTo(w.toDouble(), 0.0)
                componentShape.lineTo(w.toDouble(), h.toDouble())
                componentShape.lineTo(h / 2.0, h.toDouble())
                componentShape.curveTo(h / 2.0, h.toDouble(), 0.0, h.toDouble(), 0.0, h / 2.0)
                componentShape.curveTo(0.0, h / 2.0, 0.0, 0.0, h / 2.0, 0.0)
            }

            else -> {
                componentShape.moveTo(0.0, h.toDouble())
                componentShape.lineTo(0.0, 0.0)
                componentShape.lineTo(w.toDouble(), 0.0)
                componentShape.lineTo(w.toDouble(), h.toDouble())
                componentShape.lineTo(0.0, h.toDouble())
            }
        }


        /**
         * Create a new Area object using the rectangular `GeneralPath`,
         * and assign it to the object's `area` variable.
         */
        area = Area(componentShape)

        return componentShape
    }


    companion object {
        /**
         *
         */
        private const val serialVersionUID = 8251726686557724440L
    }
}
