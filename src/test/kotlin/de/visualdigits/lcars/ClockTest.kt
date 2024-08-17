package de.visualdigits.lcars

import de.visualdigits.lcars.elements.LCARSPanel
import de.visualdigits.lcars.panels.LClockPanel
import de.visualdigits.lcars.panels.LoginPanel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.SwingUtilities

class ClockTest : JFrame() {
    var loginPanel: LoginPanel? = null
    var loginAction: ActionListener? = null
    var logoutAction: ActionListener? = null

    init {
        /**
         * Set up the frame as maximized and undecorated with a title of "LCARS Main",
         * and EXIT_ON_CLOSE as the default close operation.
         */

        this.contentPane.background = Color.BLACK
        this.title = "LCARS Clock Test"
        this.isUndecorated = true
        this.extendedState = MAXIMIZED_BOTH
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.isVisible = true

        /**
         * Create the LCARS clock panel to test.
         */
        val lcp = LClockPanel()
        lcp.preferredSize = Dimension(LCARSPanel.PREFERRED_WIDTH, LCARSPanel.PREFERRED_HEIGHT)
        lcp.setTitle("LCARS Clock")
        this.contentPane.add(lcp)


        validate()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ClockTest::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater { ClockTest() }
        }
    }
}
