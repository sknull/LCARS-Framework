package de.visualdigits.lcars.awt.elements

import de.visualdigits.lcars.awt.LCARS
import java.awt.FontMetrics
import java.awt.geom.Area
import java.awt.geom.GeneralPath

open class LCARSLabel(
    text: String,
    x: Int,
    y: Int,
    style: Int
) : LCARSComponent(text, x, y, 0, 0, style or LCARS.ES_STATIC) {

    /**
     * The constructor of the `LCARSLabel` class.
     *
     * @param text
     * @param x
     * @param y
     * @param style
     */
    init {
        /**
         * Call the super class constructor. Initializing the object with a zero
         * width and height, and make sure the LCARS style contains
         * `LCARS.ES_STATIC`.
         */
        /**
         * Derive the font size from the `style` argument, and set
         * the `fontSize` class variable.
         */
        fontSize = LCARS.getLCARSFontSize(style)

        /**
         * Get the width and height of the text string based the font metrics of the
         * LCARS font.
         */
        val fm: FontMetrics = getFontMetrics(LCARSFont.deriveFont(fontSize))
        w = fm.stringWidth(text)
        h = fm.height

        /**
         * Create the shape of the label component.
         */
        createLabelShape()

        /**
         * Initialize the bounds of the label object to location (0, 0) and the width
         * and height derived for the component's text string.
         */
        setBounds(0, 0, w, h)

        /**
         * Set the text insets to static final defaults.
         */
        setTextInsets(0, -10)

        /**
         * Set the component's foreground to the color derived from the LCARS style.
         */
        setForeground(LCARS.getLCARSColor(style))
        setBackground(transparent)
    }


    /**
     * Create a rectangular shape based on the width and height of the label
     * component. The shape is used to create the object's `area` variable.
     *
     * @return the `GeneralPath` object created for the parent object's `area`
     */
    protected fun createLabelShape(): GeneralPath {
        /**
         * Create a new GeneralPath object.
         */
        val componentShape: GeneralPath = GeneralPath()

        /**
         * Build a simple rectangle shape using the `GeneralPath`
         * based on the dimension arguments.
         */
        componentShape.moveTo(0.0, h.toDouble())
        componentShape.lineTo(0.0, 0.0)
        componentShape.lineTo(w.toDouble(), 0.0)
        componentShape.lineTo(w.toDouble(), h.toDouble())
        componentShape.lineTo(0.0, h.toDouble())

        /**
         * Create a new Area object using the rectangular `GeneralPath`,
         * and assign it to the object's `area` variable.
         */
        area = Area(componentShape)
        areaChanged = true

        /**
         * Return the `GeneralPath` object just in case someone wants to
         * use it.
         */
        return componentShape
    }


    fun getTtext(): String? = super.componentText

    override fun setText(text: String) {
        /**
         * Set the `text` variable of the super class.
         */
        super.componentText = text

        /**
         * Get the width and height of the text string based the font metrics of the
         * LCARS font.
         */
        val fm: FontMetrics = getFontMetrics(LCARSFont.deriveFont(fontSize))
        w = fm.stringWidth(text)
        h = fm.height

        /**
         * Create the shape of the label component.
         */
        createLabelShape()
    }

    companion object {
        /**
         * The generated serial version identifier.
         */
        private const val serialVersionUID = 1775120001615158577L
    }
}
