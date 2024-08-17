/**
 *
 */
package de.visualdigits.lcars.elements

/**
 * @author Perry Spagnola
 */
class LCARSButton : LCARSRectangle {
    constructor(text: String?, x: Int, y: Int, style: Int) : super(x, y,
        defaultWidth,
        defaultHeight, style) {
        init(text)
    }

    private fun init(text: String?) {
        setTextInsets(h / 3, 10)

        super.componentText = text
    }

    /**
     * @param x
     * @param y
     * @param w
     * @param h
     * @param style
     */
    constructor(text: String?, x: Int, y: Int, w: Int, h: Int, style: Int) : super(x, y, w, h, style) {
        init(text)
    }

    companion object {
        const val defaultWidth: Int = 150
        const val defaultHeight: Int = 60
    }
}
