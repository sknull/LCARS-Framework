package de.visualdigits.lcars.awt.frame

import de.visualdigits.lcars.awt.panels.LCARSApplicationPanel
import de.visualdigits.lcars.awt.panels.LMainPanel
import de.visualdigits.lcars.awt.panels.LoginPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

class LCARSFrame : JFrame() {
    var loginPanel: LoginPanel
    var loginAction: ActionListener
    var logoutAction: ActionListener
    var mainPanel: LMainPanel
    var p1: JPanel
    var p2: JPanel
    var p3: JPanel

    init {
        /**
         * Set up the frame as maximized and undecorated with a title of "LCARS Main",
         * and EXIT_ON_CLOSE as the default close operation.
         */

        this.contentPane.setBackground(Color.BLACK)
        this.setTitle("LCARS Main")
        this.isUndecorated = true
        this.setExtendedState(MAXIMIZED_BOTH)
        this.setDefaultCloseOperation(EXIT_ON_CLOSE)
        this.isVisible = true

        /**
         * Create action listeners for "Login" and Logout".
         */
        loginAction = ActionListener { login() }
        loginPanel = LoginPanel()
        loginPanel.preferredSize = Dimension(1920, 1080)
        //loginPanel.setPreferredSize(new Dimension(1680, 985));
        loginPanel.setTitle("Login Title")
        loginPanel.setTitle("SECOND Login Title")

        logoutAction = ActionListener { logout() }
        mainPanel = LMainPanel(logoutAction)

        this.contentPane.add(loginPanel)


        val b1: JButton = JButton("Test Panel 1")
        val b2: JButton = JButton("Test Panel 2")
        val b3: JButton = JButton("Test Panel 3")

        b1.addActionListener { switchPanels(1) }

        b2.addActionListener { switchPanels(2) }

        b3.addActionListener { switchPanels(3) }

        p1 = LCARSApplicationPanel()
        p2 = LCARSApplicationPanel()
        p3 = LCARSApplicationPanel()


        p1.add(JLabel("Test Panel 1"))
        p2.add(JLabel("Test Panel 2"))
        p3.add(JLabel("Test Panel 3"))

        mainPanel.add(b1)
        mainPanel.add(b2)
        mainPanel.add(b3)

        //this.getContentPane().add(mainPanel);
        this.validate()


        //this.getContentPane().remove(loginPanel);
        //this.validate();
    }

    fun switchPanels(activePanel: Int) {
        mainPanel.isVisible = false

        if (activePanel == 1) {
            p1.isVisible = true
            if (!this.contentPane.isAncestorOf(p1)) {
                LOGGER.info("Adding p1 to the content pane...")
                this.contentPane.add(p1)
            }
        } else if (activePanel == 2) {
            p2.isVisible = true
            this.contentPane.add(p2)
        } else if (activePanel == 3) {
            p3.isVisible = true
            this.contentPane.add(p3)
        }
    }

    fun login() {
        loginPanel.isVisible = false
        if (!this.contentPane.isAncestorOf(this.mainPanel)) {
            LOGGER.info("Adding Main Panel to the content pane...")
            this.contentPane.add(this.mainPanel)
        } else {
            mainPanel.isVisible = true
        }
    }

    fun logout() {
        mainPanel.isVisible = false
        loginPanel.isVisible = true
    }

    companion object {
        /**
         * The generated serial version identifier.
         */
        private const val serialVersionUID = -6830991429593466613L

        private val LOGGER: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

        fun main(args: Array<String?>?) {
            SwingUtilities.invokeLater { LCARSFrame() }
        }
    }
}
