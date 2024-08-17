/**
 *
 */
package de.visualdigits.lcars.awt.elements


import de.visualdigits.lcars.awt.LCARS
import java.awt.FontMetrics
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask

/**
 * @author Perry Spagnola
 */
class LCARSTime : LCARSLabel {

    private var format: SimpleDateFormat

    /**
     * @param parent
     * @param x
     * @param y
     * @param style
     */
    constructor(x: Int, y: Int, style: Int) : super("00:00", x, y, style) {
        format = formattedTime[DEFAULT]

        init()
    }

    private fun init() {
        componentText = format.format(Calendar.getInstance().time)
        timeZone = Calendar.getInstance().getTimeZone()

        val fm: FontMetrics = getFontMetrics(LCARSFont.deriveFont(fontSize))
        w = fm.stringWidth(componentText)
        h = fm.height
        createLabelShape()

        setBounds(0, 0, w, h)

        setTextInsets(0, -10)

        //setForeground(LCARS.getLCARSColor(style));
        (Timer()).schedule(object : TimerTask() {
            override fun run() {
                format.timeZone = timeZone
                componentText = format.format(Calendar.getInstance().time)
                repaint()
            }
        }, LCARS.SECOND.toLong(), LCARS.SECOND.toLong())
    }


    constructor(x: Int, y: Int, mode: Int, size: Int, style: Int) : super("00:00", x, y, style) {
        fontSize = size.toFloat()

        format = formattedTime[mode]

        init()
    }

    fun setMode(mode: Int) {
        format = formattedTime[mode]
    }

    companion object {
        const val DEFAULT: Int = 0
        const val DAY_OF_WEEK: Int = 1
        const val AM_PM: Int = 2
        const val DATE: Int = 3
        const val TIME_ZONE: Int = 4
        const val TIME_ZONE_RFC822: Int = 5
        const val TIME: Int = 6
        const val SECONDS: Int = 7
        const val TIME_24: Int = 8
        private val formattedTime: Array<SimpleDateFormat> = arrayOf<SimpleDateFormat>(
            SimpleDateFormat("HH:mm:ss"),
            SimpleDateFormat("EEEE"),
            SimpleDateFormat("a"),
            SimpleDateFormat("MMMM d, yyyy"),
            SimpleDateFormat("zzzz"),
            SimpleDateFormat("Z"),
            SimpleDateFormat("h:mm"),
            SimpleDateFormat("ss"),
            SimpleDateFormat("HH:mm")
        )
        var timeZone: TimeZone? = Calendar.getInstance().getTimeZone()

        fun setTimeZone(timezone: String? = null) {
            timeZone = timezone?.let { TimeZone.getTimeZone(it) } ?: Calendar.getInstance().getTimeZone()
        }
    }
}
