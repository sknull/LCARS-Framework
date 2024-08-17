/**
 *
 */
package de.visualdigits.lcars.elements

import de.visualdigits.lcars.LCARS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.FontMetrics
import java.util.Calendar
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask

/**
 * @author spagnola
 */
class LCARSAlarm(x: Int, y: Int, style: Int) : LCARSLabel("00:00", x, y, style) {

    private var mode: Int = 0
    private var alarmHour: Int = 0
    private var alarmMinute: Int = 0

    constructor(x: Int, y: Int, fontSize: Int, style: Int): this(x, y, style) {
        super.fontSize = fontSize.toFloat()
    }

    init {
        init()
    }

    private fun init() {
        mode = MODE_12_HOUR
        updateDisplay()
        timeZone = Calendar.getInstance().getTimeZone()

        val fm: FontMetrics = getFontMetrics(LCARSFont.deriveFont(fontSize))
        w = fm.stringWidth(componentText)
        h = fm.height
        createLabelShape()

        setBounds(0, 0, w, h)

        setTextInsets(0, -25)

        (Timer()).schedule(object : TimerTask() {
            override fun run() {
                //setText(format.format(Calendar.getInstance().getTime()));
                //repaint();
            }
        }, LCARS.SECOND.toLong(), LCARS.SECOND.toLong())
    }

    fun updateDisplay() {
        String.format("%2d:%02d", alarmHour, alarmMinute)

        if (mode == MODE_24_HOUR) {
            componentText = String.format("%02d:%02d", alarmHour, alarmMinute)
        } else {
            var displayHour = alarmHour
            if (displayHour > 12) {
                displayHour = alarmHour - 12
            }
            componentText = String.format("%2d:%02d %s", displayHour, alarmMinute, AM_PM[alarmHour / 12])
        }

        repaint()
    }

    fun on() {
    }

    fun off() {
    }

    fun snooze() {
    }

    fun incrementAlarmHour() {
        alarmHour = if (alarmHour == maxHour) {
            0
        } else {
            alarmHour + 1
        }

        updateDisplay()
    }

    fun decrementAlarmHour() {
        alarmHour = if (alarmHour == 0) {
            maxHour
        } else {
            alarmHour - 1
        }

        updateDisplay()
    }

    fun incrementAlarmMinute() {
        alarmMinute = if (alarmMinute == 59) {
            0
        } else {
            alarmMinute + 1
        }

        updateDisplay()
    }

    fun decrementAlarmMinute() {
        alarmMinute = if (alarmMinute == 0) {
            59
        } else {
            alarmMinute - 1
        }

        updateDisplay()
    }

    fun getMode(): Int {
        return mode
    }

    fun setMode(mode: Int) {
        if (mode == MODE_24_HOUR || mode == MODE_12_HOUR) {
            this.mode = mode
        } else {
            this.mode = MODE_12_HOUR
            LOGGER.warn("Attempt to set invalid time mode value: $mode")
        }
    }

    fun toggleMode() {
        if (mode == MODE_24_HOUR) {
            mode = MODE_12_HOUR
        } else if (mode == MODE_12_HOUR) {
            mode = MODE_24_HOUR
        } else {
            LOGGER.warn("invalid time mode value: $mode - reset to MODE_12_HOUR")
            mode = MODE_12_HOUR
        }

        updateDisplay()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARSAlarm::class.java)

        const val MODE_24_HOUR: Int = 0
        const val MODE_12_HOUR: Int = 1

        private val AM_PM: Array<String> = arrayOf("AM", "PM")
        private const val maxHour: Int = 23
        var timeZone: TimeZone? = null

        fun setTimeZone(timezone: String? = null) {
            timeZone = timezone?.let { TimeZone.getTimeZone(it) }?:Calendar.getInstance().getTimeZone()
        }
    }
}
