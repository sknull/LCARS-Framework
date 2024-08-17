package de.visualdigits.lcars.panels

import de.visualdigits.lcars.LCARS
import de.visualdigits.lcars.elements.LCARSAlarm
import de.visualdigits.lcars.elements.LCARSButton
import de.visualdigits.lcars.elements.LCARSCalendarPane
import de.visualdigits.lcars.elements.LCARSCorner
import de.visualdigits.lcars.elements.LCARSLabel
import de.visualdigits.lcars.elements.LCARSPanel
import de.visualdigits.lcars.elements.LCARSRectangle
import de.visualdigits.lcars.elements.LCARSTime
import java.awt.event.ActionListener
import java.util.TimeZone

class LClockPanel : LCARSPanel() {

    private var time: LCARSTime
    private var time24: LCARSTime
    private var timeWithSeconds: LCARSTime
    private var amPm: LCARSTime
    private var date: LCARSTime
    private var dayOfWeek: LCARSTime
    private var seconds: LCARSTime
    private var timeZone: LCARSTime
    private var mode: Int = 1
    private var calendar: LCARSCalendarPane

    private var alarm: LCARSAlarm = LCARSAlarm(200, 460, 150, LCARS.EC_ORANGE or LCARS.ES_LABEL_NW)

    init {
        /**
         * Clock Panel statistics
         */
        val titleRect: LCARSRectangle =
            LCARSRectangle(10, 10, 150, 60, LCARS.EC_ORANGE or LCARS.ES_STATIC)
        add(titleRect)

        var label: LCARSLabel = LCARSLabel(
            "HOST NAME :",
            1500,
            100,
            LCARS.EC_ORANGE or LCARS.ES_LABEL_C or LCARS.EF_BUTTON
        )
        add(label)

        label = LCARSLabel(
            LCARS.getHostName(),
            1700,
            100,
            LCARS.EC_ORANGE or LCARS.ES_LABEL_C or LCARS.EF_BUTTON
        )
        add(label)

        label = LCARSLabel(
            "IP ADDRESS :",
            1500,
            130,
            LCARS.EC_ORANGE or LCARS.ES_LABEL_C or LCARS.EF_BUTTON
        )
        add(label)

        label = LCARSLabel(
            LCARS.getIPAddress()!!,
            1700,
            130,
            LCARS.EC_ORANGE or LCARS.ES_LABEL_C or LCARS.EF_BUTTON
        )
        add(label)

        /**
         * Calendar pane and controls
         */
        calendar = LCARSCalendarPane(200, 70, LCARS.EF_BODY)
        add(calendar)

        var lb =
            LCARSButton("Next", 10, 75, LCARS.EF_BUTTON or LCARS.EC_L_BLUE)
        lb.setName("Next Button")
        lb.actionListener = ActionListener { calendar.incrementMonth() }
        add(lb)

        lb = LCARSButton("", 10, 140, LCARS.EF_BUTTON or LCARS.EC_L_BLUE)
        lb.setName("This Month Button")
        lb.actionListener = ActionListener { calendar.thisMonth() }
        add(lb)

        lb = LCARSButton("Previous", 10, 205, LCARS.EF_BUTTON or LCARS.EC_L_BLUE)
        lb.setName("Previous Button")
        lb.actionListener = ActionListener { calendar.decrementMonth() }
        add(lb)

        /**
         * Frame components
         */
        val calendarFrame: LCARSCorner = LCARSCorner(
            10, 270, 650, 90,
            style = LCARS.EC_L_BLUE or LCARS.ES_SHAPE_SW or LCARS.ES_STATIC
        )
        calendarFrame.setName("calendar")
        add(calendarFrame)

        val clockFrame: LCARSCorner = LCARSCorner(
            10, 365, 650, 90,
            style = LCARS.EC_ORANGE or LCARS.ES_SHAPE_NW or LCARS.ES_STATIC
        )
        clockFrame.setName("clock")
        add(clockFrame)

        var rect: LCARSRectangle =
            LCARSRectangle(665, 320, 30, 40, LCARS.EC_ORANGE or LCARS.ES_STATIC)
        add(rect)

        rect = LCARSRectangle(700, 340, 775, 20, LCARS.EC_YELLOW or LCARS.ES_STATIC)
        add(rect)

        rect = LCARSRectangle(700, 365, 775, 20, LCARS.EC_YELLOW or LCARS.ES_STATIC)
        add(rect)

        rect = LCARSRectangle(1480, 330, 430, 30, LCARS.EC_ORANGE or LCARS.ES_STATIC)
        add(rect)

        rect = LCARSRectangle(1480, 365, 430, 30, LCARS.EC_M_BLUE or LCARS.ES_STATIC)
        add(rect)

        rect = LCARSRectangle(665, 365, 30, 40, LCARS.EC_ORANGE or LCARS.ES_STATIC)
        add(rect)

        /**
         * Time fields including: basic time, AM/PM designation, seconds, day, date,
         * time zone, 24 hour basic time, and 24 hour time with seconds.
         */
        timeWithSeconds = LCARSTime(
            725,
            450,
            LCARSTime.DEFAULT,
            500,
            LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
        )
        add(timeWithSeconds)
        timeWithSeconds.isVisible = false

        time =
            LCARSTime(
                725,
                450,
                LCARSTime.TIME,
                500,
                LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
            )
        add(time)

        time24 = LCARSTime(
            725,
            450,
            LCARSTime.TIME_24,
            500,
            LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
        )
        add(time24)
        time24.isVisible = false

        seconds = LCARSTime(
            1600,
            450,
            LCARSTime.SECONDS,
            240,
            LCARS.EC_M_BLUE or LCARS.ES_LABEL_NW
        )
        add(seconds)

        amPm = LCARSTime(
            1600,
            685,
            LCARSTime.AM_PM,
            240,
            LCARS.EC_M_BLUE or LCARS.ES_LABEL_NW
        )
        add(amPm)

        dayOfWeek = LCARSTime(
            825,
            50,
            LCARSTime.DAY_OF_WEEK,
            175,
            LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
        )
        add(dayOfWeek)

        date =
            LCARSTime(
                825,
                250,
                LCARSTime.DATE,
                75,
                LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
            )
        add(date)

        timeZone = LCARSTime(
            825,
            1000,
            LCARSTime.TIME_ZONE,
            75,
            LCARS.EC_YELLOW or LCARS.ES_LABEL_NW
        )
        add(timeZone)

        /**
         * Time Zone controls
         */
        lb = LCARSButton(
            "Previous", 1480, 1000,
            LCARS.ES_LABEL_C or LCARS.ES_RECT_RND_W or LCARS.EF_BUTTON or LCARS.EC_M_BLUE
        )
        lb.setName("Previous Time Zone")
        lb.actionListener = ActionListener { decrementTimeZone() }
        add(lb)

        lb = LCARSButton(
            "TIME ZONE", 1635, 1000, 100, LCARSButton.defaultHeight,
            LCARS.ES_LABEL_C or LCARS.EF_BUTTON or LCARS.EC_M_BLUE
        )
        lb.setName("This Time Zone")
        lb.actionListener = ActionListener { setLocalTimeZone() }
        add(lb)

        lb = LCARSButton(
            "Next", 1740, 1000,
            LCARS.ES_LABEL_C or LCARS.ES_RECT_RND_E or LCARS.EF_BUTTON or LCARS.EC_M_BLUE
        )
        lb.setName("Next Time Zone")
        lb.actionListener = ActionListener { incrementTimeZone() }
        add(lb)


        /**
         * Alarm controls
         */
        lb = LCARSButton(
            "Alarm On", 10, 460,
            LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.setName("Alarm On")
        lb.actionListener = ActionListener { alarm.on() }
        add(lb)

        lb = LCARSButton(
            "Alarm Off", 10, 525,
            LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.setName("Alarm Off")
        lb.actionListener = ActionListener { alarm.off() }
        add(lb)

        lb = LCARSButton(
            "Snooze", 10, 590, LCARSButton.defaultWidth, 340,
            LCARS.ES_LABEL_C or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.setName("Snooze")
        lb.actionListener = ActionListener { alarm.snooze() }
        add(lb)

        add(alarm)


        lb = LCARSButton(
            "12 / 24", 450, 670,
            LCARS.ES_RECT_RND_E or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.actionListener = ActionListener { alarm.toggleMode() }
        add(lb)

        lb = LCARSButton(
            "Hours +", 450, 735,
            LCARS.ES_RECT_RND_E or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.actionListener = ActionListener { alarm.incrementAlarmHour() }
        add(lb)

        lb = LCARSButton(
            "Minutes +", 450, 800,
            LCARS.ES_RECT_RND_E or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.actionListener = ActionListener { alarm.incrementAlarmMinute() }
        add(lb)


        lb = LCARSButton(
            "Hours --", 200, 735,
            LCARS.ES_RECT_RND_W or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.actionListener = ActionListener { alarm.decrementAlarmHour() }
        add(lb)

        lb = LCARSButton(
            "Minutes --", 200, 800,
            LCARS.ES_RECT_RND_W or LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.actionListener = ActionListener { alarm.decrementAlarmMinute() }
        add(lb)


        /**
         * Time mode select control
         */
        lb = LCARSButton(
            "Mode Select", 10, 935,
            LCARS.EF_BUTTON or LCARS.EC_YELLOW
        )
        lb.setName("Exit")
        lb.actionListener = ActionListener { e -> nextMode() }
        add(lb)


        /**
         * Panel exit control
         */
        lb = LCARSButton(
            "Exit", 10, 1000,
            LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.setName("Exit")
        lb.actionListener = ActionListener { e -> exit() }
        add(lb)
    }


    fun incrementTimeZone() {
        val _tz: TimeZone = LCARSTime.timeZone!!

        val rawOffset: Int = _tz.rawOffset

        val tzid: Array<String?> = TimeZone.getAvailableIDs(rawOffset + LCARS.MILISECS_IN_HOUR)

        if (tzid[0] != null) {
            LCARSTime.setTimeZone(tzid[0])
            calendar.setTimeZone(tzid[0])
        }
    }


    fun decrementTimeZone() {
        val _tz: TimeZone = LCARSTime.timeZone!!

        val rawOffset: Int = _tz.rawOffset

        val tzid: Array<String?> = TimeZone.getAvailableIDs(rawOffset - LCARS.MILISECS_IN_HOUR)

        if (tzid[0] != null) {
            LCARSTime.setTimeZone(tzid[0])
            calendar.setTimeZone(tzid[0])
        }
    }


    fun setLocalTimeZone() {
        LCARSTime.setTimeZone()
        calendar.setTimeZone()
    }

    fun nextMode() {
        mode = if (mode == NUM_MODES) {
            1
        } else {
            mode + 1
        }

        setMode()
    }

    /**
     *
     */
    fun setMode() {
        /**
         * Only process valid modes.
         */
        when (mode) {
            TIME_12 -> {
                time24.isVisible = false
                timeWithSeconds.isVisible = false
                time.isVisible = true
                amPm.isVisible = true
                seconds.isVisible = true
            }

            TIME_24 -> {
                time.isVisible = false
                timeWithSeconds.isVisible = false
                amPm.isVisible = false
                time24.isVisible = true
                seconds.isVisible = true
            }

            TIME_WITH_SECONDS -> {
                time.isVisible = false
                time24.isVisible = false
                amPm.isVisible = false
                seconds.isVisible = false
                timeWithSeconds.isVisible = true
            }
        }
    }

    companion object {
        private const val NUM_MODES: Int = 3
        private const val TIME_12: Int = 1
        private const val TIME_24: Int = 2
        private const val TIME_WITH_SECONDS: Int = 3
    }
}
