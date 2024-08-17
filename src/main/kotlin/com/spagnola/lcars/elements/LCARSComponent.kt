package com.spagnola.lcars.elements

import com.spagnola.lcars.LCARS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import javax.swing.JComponent
import javax.swing.event.MouseInputListener

/**
 * @author Perry Spagnola
 */
abstract class LCARSComponent(
    open var componentText: String? = "",
    private var x: Int,
    private var y: Int,
    var w: Int,
    var h: Int,
    var style: Int
) :
    JComponent(), MouseInputListener, ComponentListener {
    /**
     * Simple accessor method to get the component's displayable text.
     *
     * @return the displayed text of the component
     */
    /**
     * Simple accessor method to set the component's displayed text. Set to `""`
     * as a default for blank text
     *
     * @param text the text to be displayed on the component, cannot be null
     */


    /**
     * Simple accessor method to get the x-axis inset for the component's displayed text.
     *
     * @return the x-axis offset inside of the component shape's bounds
     */
    protected var textInsetX: Int = 10

    /**
     * Simple accessor method to get the y-axis inset for the component's displayed text.
     *
     * @return the y-axis offset inside of the component shape's bounds
     */
    protected var textInsetY: Int = 10
    protected var textX: Int = 0
    protected var textY: Int = 0
    protected var scaleFactor: Double = 0.0
    protected var area: Area? = null
    protected var scaledArea: Area? = null
    protected var areaChanged: Boolean = false
    private var normalColor: Color = LCARS.getLCARSColor(style)
    private var pressedColor: Color = normalColor.darker()
    private var hoverColor: Color = normalColor.brighter()
    protected var LCARSFont: Font
    private var scaledLCARSFont: Font? = null
    protected var fontSize: Float = 0f

    private var renderingTransform: AffineTransform? = null

    var actionListener: ActionListener? = null

    init {
        setBackground(normalColor)
        setForeground(LCARS.getLCARSTextColor(style))

        /**
         * Get the LCARS font from the LCARS singleton object.
         */
        LCARSFont = LCARS.getSingletonObject().LCARSFont

        setFontSize()

        setBounds(0, 0, w, h)

        textInsetX = 10
        textInsetY = 10

        /**
         * Only add a mouse listener if the `style` argument does not
         * contain `LCARS.ES_STATIC`.
         */
        if (!isStatic()) {
            addMouseListener(this)
            addMouseMotionListener(this)
        }


        //addComponentListener(this);
    }

    open fun setText(text: String) {
        componentText = text
    }

    private fun setFontSize() {
        fontSize = LCARS.getLCARSFontSize(style)
        if (fontSize == 0f) {
            fontSize = LCARS.getLCARSFontSize(LCARS.EF_BUTTON)
        }
    }

    fun isStatic(): Boolean = LCARS.isStatic(style)

    override fun paintComponent(g: Graphics) {
        /**
         * First, paint the background if the component is opaque. Required when
         * JComponent is extended, and the paintCompnent() method is overridden.
         */

        if (isOpaque) {
            g.color = getBackground()
            g.fillRect(0, 0, width, height)
        }

        /**
         * Create a Graphics2D object so we can use Java 2D features.
         */
        val g2d: Graphics2D = g.create() as Graphics2D

        /**
         * Set the font, the anti aliasing rendering hint, and the color to draw
         * the component shape with.
         */
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.color = getBackground()
        g2d.font = scaledLCARSFont

        /**
         * Scale the LCARS component, both shape and text.
         */
        scaleComponent(g2d)

        /**
         * Draw the scaled Area object of the LCARS component, and fill it.
         */
        g2d.draw(scaledArea)
        g2d.fill(scaledArea)

        /**
         * Set the color to draw the font, and draw the component text.
         */
        g2d.color = getForeground()
        g2d.drawString(componentText, textX, textY)

        /**
         * Clean up when the method has completed by disposing the Graphics2D object that was created.
         */
        g2d.dispose()
    }

    private fun scaleComponent(g2d: Graphics2D) {
        val sw = parent.width.toDouble() / parent.preferredSize.width.toDouble()
        val sh = parent.height.toDouble() / parent.preferredSize.height.toDouble()

        val scaleFactor: Double

        scaleFactor = if (sh < sw) {
            sh
        } else {
            sw
        }

        if (scaleFactor != this.scaleFactor || areaChanged) {
            this.scaleFactor = scaleFactor
            areaChanged = false

            scaledArea = Area(area)
            renderingTransform = AffineTransform()
            renderingTransform!!.scale(scaleFactor, scaleFactor)
            scaledArea!!.transform(renderingTransform)

            scaledLCARSFont = LCARSFont.deriveFont(fontSize * scaleFactor.toFloat())

            /**
             * Calculate x-axis and y-axis offsets to center the scaled component within the display.
             */
            val _x = ((parent.width - parent.preferredSize.width * scaleFactor) / 2).toInt()
            val _y = ((parent.height - parent.preferredSize.height * scaleFactor) / 2).toInt()

            /**
             * Set the bounds of the component to set its on-screen position and make it visible.
             */
            setBounds(
                (x * scaleFactor).toInt() + _x,
                (y * scaleFactor).toInt() + _y,
                scaledArea!!.bounds.width,
                scaledArea!!.bounds.height
            )
        }

        /**
         * Set the text position of the scaled component.
         */
        setTextPosition(g2d)
    }

    protected open fun setTextPosition(g2d: Graphics2D) {
        /**
         * Extract the component's text position from the style using the
         * `LCARS.ES_LABEL` mask.
         */

        val textPosition = style and LCARS.ES_LABEL

        /**
         * Get the font metrics and the bounding rectangle for the component's text
         * for use calculating the text position.
         */
        val fm: FontMetrics = g2d.fontMetrics
        fm.stringWidth(componentText)
        val r: Rectangle2D = fm.getStringBounds(componentText, g2d)


        /**
         * Conditionally calculate and set the `y` position of the component text.
         * The switch statement groups together the north, south and horizontal centers.
         */
        when (textPosition) {
            LCARS.ES_LABEL_NW, LCARS.ES_LABEL_N, LCARS.ES_LABEL_NE -> textY =
                (fm.ascent + textInsetY * scaleFactor).toInt()

            LCARS.ES_LABEL_SW, LCARS.ES_LABEL_S, LCARS.ES_LABEL_SE -> textY =
                (scaledArea!!.bounds.height - textInsetY * scaleFactor).toInt()

            LCARS.ES_LABEL_W, LCARS.ES_LABEL_C, LCARS.ES_LABEL_E -> textY =
                scaledArea!!.bounds.height / 2 + fm.ascent / 2

            else ->
                /**
                 * Log an error for the `y` position, if there was no text position specified
                 * or an invalid position specified.
                 */
                LOGGER.info("No LCARS text position selected, y position not set.")
        }

        /**
         * Conditionally calculate and set the `x` position of the component text.
         * The switch statement groups together the east, west and vertical centers.
         */
        when (textPosition) {
            LCARS.ES_LABEL_NW, LCARS.ES_LABEL_W, LCARS.ES_LABEL_SW -> textX =
                (textInsetX * scaleFactor).toInt()

            LCARS.ES_LABEL_NE, LCARS.ES_LABEL_E, LCARS.ES_LABEL_SE -> textX =
                (scaledArea!!.bounds.width - r.width - textInsetX * scaleFactor).toInt()

            LCARS.ES_LABEL_N, LCARS.ES_LABEL_C, LCARS.ES_LABEL_S -> textX =
                (scaledArea!!.bounds.width / 2 - r.width / 2).toInt()

            else ->
                /**
                 * Log an error for the `x` position, if there was no text position specified
                 * or an invalid position specified.
                 */
                LOGGER.info("No LCARS text position selected, x position not set.")
        }
    }

    /**
     * Simple accessor method to set the insets for the component's displayed text.
     *
     * @param x the x-axis offset inside of the component shape's bounds
     * @param y the y-axis offset inside of the component shape's bounds
     */
    fun setTextInsets(x: Int, y: Int) {
        this.textInsetX = x
        this.textInsetY = y
    }

    override fun mouseClicked(e: MouseEvent) {
        //Point pt = e.getPoint();
        //LOGGER.info("mouseClicked: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

        if (scaledArea!!.contains(e.getPoint())) {
            if (actionListener != null) {
                actionListener!!.actionPerformed(ActionEvent(e.source, e.id, e.paramString()))
            }
        }
    }

    override fun mousePressed(e: MouseEvent) {
        // TODO Auto-generated method stub
        if (scaledArea!!.contains(e.getPoint())) {
            //Point pt = e.getPoint();
            //LOGGER.info("mousePressed: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

            setBackground(pressedColor)
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        // TODO Auto-generated method stub
        if (scaledArea!!.contains(e.getPoint())) {
            //Point pt = e.getPoint();
            //LOGGER.info("mouseReleased: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

            setBackground(hoverColor)
        }
    }

    override fun mouseEntered(e: MouseEvent) {
        // TODO Auto-generated method stub
        if (scaledArea!!.contains(e.getPoint())) {
            //Point pt = e.getPoint();
            //LOGGER.info("mouseEntered: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

            setBackground(hoverColor)
        }
    }

    override fun mouseExited(e: MouseEvent) {
        // TODO Auto-generated method stub
        if (!scaledArea!!.contains(e.getPoint())) {
            //Point pt = e.getPoint();
            //LOGGER.info("mouseExited: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

            setBackground(normalColor)
        }
    }

    override fun mouseDragged(e: MouseEvent?) {
        // TODO Auto-generated method stub
        System.out.println("mouseDragged()...")
    }

    override fun mouseMoved(e: MouseEvent) {
        // TODO Auto-generated method stub
        if (scaledArea!!.contains(e.getPoint())) {
            //Point pt = e.getPoint();
            //LOGGER.info("mouseMoved: " + this.getName() + " - " + pt.getX() + ", " + pt.getY());

            setBackground(hoverColor)
        } else {
            setBackground(normalColor)
        }
    }

    override fun componentResized(e: ComponentEvent?) {
        // TODO Auto-generated method stub
        //LOGGER.info("componentResized: " + getName());
        //getParent().paintComponents(getParent().getGraphics());
    }


    override fun componentMoved(e: ComponentEvent?) {
        // TODO Auto-generated method stub
        //LOGGER.info("componentMoved: " + getName());
    }


    override fun componentShown(e: ComponentEvent?) {
        // TODO Auto-generated method stub
        //LOGGER.info("componentShown");
    }


    override fun componentHidden(e: ComponentEvent?) {
        // TODO Auto-generated method stub
        //LOGGER.info("componentHidden");
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARSComponent::class.java)

        /**
         * A reference to the global Logger object.
         */
        @JvmStatic
        protected val transparent: Color = Color(0f, 0f, 0f, 0f)

        /**
         * The generated serial version identifier.
         */
        const val serialVersionUID = 5796010256072072526L
    }
}
