/**
 *
 */
package com.spagnola.lcars.elements

import com.spagnola.lcars.LCARS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

/**
 * @author Perry Spagnola
 */
class LCARSCalendarPane : LCARSTextPane {
    
    private var title: SimpleAttributeSet? = null
    private var weekday: SimpleAttributeSet? = null
    private var saturday: SimpleAttributeSet? = null
    private var sunday: SimpleAttributeSet? = null
    private var today: SimpleAttributeSet? = null

    private var currentMonth: Int = 0
    private var currentDay: Int = 0
    private var currentYear: Int = 0

    private var displayMonth: Int = 0
    private var displayYear: Int = 0

    private var daysInMonth: Int = 0

    private var dayToday: Int = 0

    private var timezone: TimeZone = Calendar.getInstance().getTimeZone()


    constructor(x: Int, y: Int, style: Int) : super(x, y, PANE_WIDTH, PANE_HEIGHT, style) {
        /**
         * Set the current date, the displayed month. The displayed month is the
         * current month.
         */
        setCurrentDate()
        setDisplayMonth(currentMonth, currentYear)

        /**
         * Initialize the calendar pane object.
         */
        init()
    }


    constructor(x: Int, y: Int, month: Int, year: Int, style: Int) : super(x, y, PANE_WIDTH, PANE_HEIGHT, style) {
        /**
         * Set the current date, the displayed month. The displayed month is
         * specified by the month and year arguments.
         */
        setCurrentDate()
        setDisplayMonth(month, year)

        /**
         * Initialize the calendar pane object.
         */
        init()
    }


    private fun init() {
        title = SimpleAttributeSet()
        weekday = SimpleAttributeSet()
        sunday = SimpleAttributeSet()
        saturday = SimpleAttributeSet()
        today = SimpleAttributeSet()

        /**
         * Set the font sizes.
         */
        StyleConstants.setFontSize(title, (LCARS.getLCARSFontSize(LCARS.EF_BUTTON) * 1.25).toInt())
        StyleConstants.setFontSize(weekday, LCARS.getLCARSFontSize(LCARS.EF_BUTTON).toInt())
        StyleConstants.setFontSize(saturday, LCARS.getLCARSFontSize(LCARS.EF_BUTTON).toInt())
        StyleConstants.setFontSize(sunday, LCARS.getLCARSFontSize(LCARS.EF_BUTTON).toInt())
        StyleConstants.setFontSize(today, LCARS.getLCARSFontSize(LCARS.EF_BUTTON).toInt())

        /**
         * Set the filed colors.
         */
        StyleConstants.setForeground(title, COLOR_WEEKDAY)
        StyleConstants.setForeground(weekday, COLOR_WEEKDAY)
        StyleConstants.setForeground(saturday, COLOR_SATURDAY)
        StyleConstants.setForeground(sunday, COLOR_SUNDAY)
        StyleConstants.setForeground(today, COLOR_TODAY)


        /**
         * Set the number of days in the currently displayed month.
         */
        setDaysInMonth()

        /**
         * Create the calendar document for the specified or current month. This is determined
         * by the constructor.
         */
        createCalendarDoc()


        (Timer()).schedule(object : TimerTask() {
            override fun run() {
                updateCalendarPane()
            }
        }, LCARS.MINUTE.toLong(), LCARS.MINUTE.toLong())
    }

    fun isWeekday(day: Int): Boolean {
        val _day = dayOfWeek(displayMonth, day, displayYear)

        return _day > 0 && _day < 6
    }

    fun setTimeZone() {
        timezone = Calendar.getInstance().getTimeZone()
        updateCalendarPane()
    }

    private fun updateCalendarPane() {
        if (dateHasChanged()) {
            setCurrentDate()
            createCalendarDoc()
            setDisplayMonth(currentMonth, currentYear)
            parent.repaint()
        }
    }

    private fun createCalendarDoc() {
        try {
            /**
             * Clear the document. Required for date roll overs.
             */
            document.remove(0, document.length)

            /**
             * Insert the calendar header of month and year.
             */
            document.insertString(
                0,
                (MONTHS[displayMonth] + HEADER_TAB_SPACE[displayMonth] + HEADER_WHITE_SPACE + displayYear).toString() + "\n",
                title
            )

            var attrs: SimpleAttributeSet? = weekday

            /**
             * Get the starting day of week for the month.
             */
            val startDay = dayOfWeek(displayMonth, 1, displayYear)

            /**
             * Insert any blank days into the first line of the calendar pane.
             */
            for (i in 0 until startDay) {
                document.insertString(document.length, "\t", attrs)
            }

            /**
             * Iterate through the days in the month.
             */
            for (i in 1..daysInMonth) {
                /**
                 * Set the text style attributes of the day numbers base on:
                 * weekday, Sunday, Saturday, and today.
                 */
                attrs = if (isSaturday(i)) {
                    saturday
                } else if (isSunday(i)) {
                    sunday
                } else {
                    weekday
                }
                if (isToday(i)) {
                    attrs = today
                }

                /**
                 * Provide a single space of left padding for the single digit
                 * dates.
                 */
                if (i < 10) {
                    document.insertString(document.length, " ", attrs)
                }

                /**
                 * Insert the appropriate date number.
                 */
                document.insertString(document.length, String.format("%2d", i), attrs)

                /**
                 * Insert a line break at the end of week and end of month. Else,
                 * insert a tab between the numbers.
                 */
                if (((i + startDay) % 7 == 0) || (i == daysInMonth)) {
                    document.insertString(document.length, "\n", attrs)
                } else {
                    document.insertString(document.length, "\t", attrs)
                }
            }
        } catch (e: Exception) {
            /**
             * Log any exceptions that are thrown during the document creation process.
             */
            LOGGER.error(e.message)
        }
    }

    fun isSunday(day: Int): Boolean {
        val _day = dayOfWeek(displayMonth, day, displayYear)

        return _day == 0
    }

    fun isSaturday(day: Int): Boolean {
        val _day = dayOfWeek(displayMonth, day, displayYear)

        return _day == 6
    }

    fun isToday(day: Int): Boolean {
        return displayYear == currentYear && displayMonth == currentMonth && day == currentDay
    }

    private fun dateHasChanged(): Boolean {
        return getCurrentDay() != currentDay || getCurrentMonth() != currentMonth
    }

    private fun getCurrentDay(): Int {
        val dayformat: SimpleDateFormat = SimpleDateFormat("d")
        dayformat.timeZone = timezone

        //LOGGER.info(dayformat.format(Calendar.getInstance().getTime()));
        return Integer.parseInt(dayformat.format(Calendar.getInstance().time))
    }

    private fun getCurrentMonth(): Int {
        val monthformat: SimpleDateFormat = SimpleDateFormat("M")
        monthformat.timeZone = timezone

        //LOGGER.info(monthformat.format(Calendar.getInstance().getTime()));
        return Integer.parseInt(monthformat.format(Calendar.getInstance().time))
    }

    private fun setCurrentDate() {
        currentDay = getCurrentDay()
        currentMonth = getCurrentMonth()
        currentYear = getCurrentYear()
    }

    private fun getCurrentYear(): Int {
        val yearformat: SimpleDateFormat = SimpleDateFormat("yyyy")
        yearformat.timeZone = timezone

        //LOGGER.info(yearformat.format(Calendar.getInstance().getTime()));
        return Integer.parseInt(yearformat.format(Calendar.getInstance().time))
    }

    private fun setDisplayMonth(month: Int, year: Int) {
        displayMonth = month
        setDaysInMonth()
        displayYear = year
    }

    private fun setDaysInMonth() {
        daysInMonth = if (displayMonth == 2 && isLeapYear()) {
            29
        } else {
            DAYS[displayMonth]
        }
    }

    /**
     * Returns true if the `currentYear` variable is a leap year.
     * @return true if the given year is a leap year, false, if not
     */
    fun isLeapYear(): Boolean {
            /**
             * If the current year is evenly divisible by 4 and not by 100, return true.
             */
            if ((displayYear % 4 == 0) && (displayYear % 100 != 0)) {
                return true
            }

            /**
             * If the current year is evenly divisible by 400, return true.
             */
            return displayYear % 400 == 0

            /**
             * If none of the leap year conditions is met, method falls through,
             * and returns false.
             */
        }

    fun setTimeZone(timezoneID: String?) {
        timezone = TimeZone.getTimeZone(timezoneID)
        updateCalendarPane()
    }

    fun incrementMonth() {
        var month = displayMonth
        var year = displayYear

        if (month == 12) {
            month = 1
            year++
        } else {
            month++
        }

        setDisplayMonth(month, year)
        createCalendarDoc()
        parent.repaint()
    }


    fun decrementMonth() {
        var month = displayMonth
        var year = displayYear

        if (month == 1) {
            month = 12
            year--
        } else {
            month--
        }

        setDisplayMonth(month, year)
        createCalendarDoc()
        parent.repaint()
    }


    fun thisMonth() {
        setDisplayMonth(currentMonth, currentYear)
        createCalendarDoc()
        parent.repaint()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARSCalendarPane::class.java)

        private val COLOR_WEEKDAY: Color = LCARS.getLCARSColor(LCARS.EC_L_BLUE)
        private val COLOR_SATURDAY: Color = LCARS.getLCARSColor(LCARS.EC_M_BLUE)
        private val COLOR_SUNDAY: Color = LCARS.getLCARSColor(LCARS.EC_RED)
        private val COLOR_TODAY: Color = LCARS.getLCARSColor(LCARS.EC_YELLOW)

        private val DAYS: IntArray = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        private val MONTHS: Array<String> = arrayOf(
            "",
            /** MONTHS[1] = "January"  */
            /** MONTHS[1] = "January"  */
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

        private val HEADER_TAB_SPACE: Array<String> = arrayOf(
            "",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t",
            "\t\t\t\t"
        )

        private const val HEADER_WHITE_SPACE: String = "           "

        private const val PANE_WIDTH: Int = 475
        private const val PANE_HEIGHT: Int = 225

        /**
         * Returns the day of the week according to the Gregorian calendar, given
         * the `month`, `day`, and `year`.
         * January through December equal 1 - 12, and Sunday through Saturday equal
         * 0 - 6.
         * @param month  the month of the date
         * @param day  the day of the date
         * @param year  the year of the date
         * @return the day of the week according to the Gregorian calendar
         */
        fun dayOfWeek(month: Int, day: Int, year: Int): Int {
            val y = year - (14 - month) / 12
            val x = y + y / 4 - y / 100 + y / 400
            val m = month + 12 * ((14 - month) / 12) - 2
            val d = (day + x + (31 * m) / 12) % 7
            return d
        }
    }
}
