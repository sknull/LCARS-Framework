/**
 *
 */
package com.spagnola.lcars.elements


import com.spagnola.lcars.LCARS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.AffineTransform
import javax.swing.JTextPane
import javax.swing.text.AbstractDocument
import javax.swing.text.BoxView
import javax.swing.text.ComponentView
import javax.swing.text.Element
import javax.swing.text.IconView
import javax.swing.text.LabelView
import javax.swing.text.ParagraphView
import javax.swing.text.Position
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import javax.swing.text.StyledEditorKit
import javax.swing.text.View
import javax.swing.text.ViewFactory

/**
 * @author Perry Spagnola
 */
open class LCARSTextPane(
    private var x: Int,
    private var y: Int,
    private var w: Int,
    private var h: Int,
    style: Int
) : JTextPane() {
    private var scaleFactor: Double = 0.0
    private var document: StyledDocument
    private var attributes: SimpleAttributeSet


    init {
        preferredSize = Dimension(w, h)
        setBounds(x, y, w, h)
        setFont(LCARS.getSingletonObject().LCARSFont)
        setBackground(transparent)
        setForeground(LCARS.getLCARSColor(style))
        isEditable = false // gets rid of the cursor
        setFocusable(false) // prevents selection highlighting

        attributes = SimpleAttributeSet()
        StyleConstants.setFontSize(attributes, LCARS.getLCARSFontSize(style).toInt())
        editorKit = ScaledEditorKit()
        document = getDocument() as StyledDocument
    }

    override fun paintComponent(g: Graphics?) {
        val scaleFactor: Double

        val sw = parent.width.toDouble() / parent.preferredSize.width.toDouble()
        val sh = parent.height.toDouble() / parent.preferredSize.height.toDouble()

        scaleFactor = if (sh < sw) {
            sh
        } else {
            sw
        }

        if (scaleFactor != this.scaleFactor) {
            this.scaleFactor = scaleFactor

            /**
             * Calculate x-axis and y-axis offsets to center the scaled component within the display.
             */
            val _x = ((parent.width - parent.preferredSize.width * scaleFactor) / 2).toInt()
            val _y = ((parent.height - parent.preferredSize.height * scaleFactor) / 2).toInt()

            /**
             * Set the bounds of the component to set its scaled on-screen position and make it visible.
             */
            setBounds(
                (x * scaleFactor).toInt() + _x,
                (y * scaleFactor).toInt() + _y,
                (getPreferredSize().width * scaleFactor).toInt(),
                (getPreferredSize().height * scaleFactor).toInt()
            )

            /**
             * Set the `ZOOM_FACTOR` of the pane's styled document to the
             * `scaleFactor` of the component.
             */
            getDocument().putProperty("ZOOM_FACTOR", scaleFactor)
        }

        super.paintComponent(g)
    }


    override fun repaint(x: Int, y: Int, width: Int, height: Int) {
        super.repaint(x, y, getWidth(), getHeight())
    }


    override fun setText(text: String?) {
        try {
            document.remove(0, document.length)
            document.insertString(0, text, attributes)
        } catch (e: Exception) {
        }
    }


    fun appendText(text: String?) {
        try {
            document.insertString(document.length, text, attributes)
        } catch (e: Exception) {
        }
    }


    fun prependText(text: String?) {
        try {
            document.insertString(0, text, attributes)
        } catch (e: Exception) {
        }
    }


    fun insertText(text: String?, offset: Int) {
        try {
            document.insertString(offset, text, attributes)
        } catch (e: Exception) {
        }
    }


    fun removeText(offset: Int, length: Int) {
        try {
            document.remove(offset, length)
        } catch (e: Exception) {
        }
    }

    companion object {
        /**
         * A reference to the global Logger object.
         */
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARSTextPane::class.java)
        private val transparent: Color = Color(0f, 0f, 0f, 0f)
    }
}

internal class ScaledEditorKit : StyledEditorKit() {

    override fun getViewFactory(): ViewFactory = StyledViewFactory()

    internal inner class StyledViewFactory : ViewFactory {
        override fun create(elem: Element): View {
            val kind: String = elem.name
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return LabelView(elem)
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return ParagraphView(elem)
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return ScaledView(elem, View.Y_AXIS)
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return ComponentView(elem)
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return IconView(elem)
                }
            }

            // default to text display
            return LabelView(elem)
        }
    }
}

internal class ScaledView(elem: Element?, axis: Int) : BoxView(elem, axis) {
    override fun paint(g: Graphics, allocation: Shape?) {
        val g2d: Graphics2D = g as Graphics2D
        val zoomFactor = getZoomFactor()
        val old: AffineTransform = g2d.transform
        g2d.scale(zoomFactor, zoomFactor)
        super.paint(g2d, allocation)
        g2d.transform = old
    }

    fun getZoomFactor(): Double {
            val scale = document.getProperty("ZOOM_FACTOR") as Double
            if (scale != null) {
                return scale
            }

            return 1.0
        }

    override fun getMinimumSpan(axis: Int): Float {
        var f: Float = super.getMinimumSpan(axis)
        f *= getZoomFactor().toFloat()
        return f
    }

    override fun getMaximumSpan(axis: Int): Float {
        var f: Float = super.getMaximumSpan(axis)
        f *= getZoomFactor().toFloat()
        return f
    }

    override fun getPreferredSpan(axis: Int): Float {
        var f: Float = super.getPreferredSpan(axis)
        f *= getZoomFactor().toFloat()
        return f
    }

    override fun layout(width: Int, height: Int) {
        super.layout(
            (width / getZoomFactor()).toInt(),
            (
                height *
                        getZoomFactor()
            ).toInt()
        )
    }

    override fun modelToView(pos: Int, a: Shape, b: Position.Bias?): Shape {
        val zoomFactor = getZoomFactor()
        var alloc: Rectangle = a.bounds
        val s: Shape = super.modelToView(pos, alloc, b)
        alloc = s.bounds
        alloc.x      = (alloc.x     * zoomFactor).toInt()
        alloc.y      = (alloc.y     * zoomFactor).toInt()
        alloc.width  = (alloc.width * zoomFactor).toInt()
        alloc.height = (alloc.height* zoomFactor).toInt()

        return alloc
    }

    override fun viewToModel(x: Float, y: Float, a: Shape, bias: Array<Position.Bias?>?): Int {
        var x = x
        var y = y
        val zoomFactor = getZoomFactor()
        val alloc: Rectangle = a.bounds
        x /= zoomFactor.toFloat()
        y /= zoomFactor.toFloat()
        alloc.x = (alloc.x / zoomFactor).toInt()
        alloc.y = (alloc.y / zoomFactor).toInt()
        alloc.width = (alloc.width / zoomFactor).toInt()
        alloc.height = (alloc.height / zoomFactor).toInt()

        return super.viewToModel(x, y, alloc, bias)
    }
}
