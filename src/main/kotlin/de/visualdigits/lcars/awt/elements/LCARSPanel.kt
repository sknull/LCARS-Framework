package de.visualdigits.lcars.awt.elements

import de.visualdigits.lcars.awt.LCARS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.FontMetrics
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JPanel

abstract class LCARSPanel : JPanel() {
    private var title: String = ""
    var subtitle: String = ""

    private var titleLabel: LCARSLabel? = null
    private var exitActionListener: ActionListener
    var exitButton: JButton

    init {
        /**
         * Set the default background color to `Color.black`.
         */
        this.setBackground(Color.black)

        /**
         * Set the panel's layout to null to facilitate absolute
         * placement of LCARS components.
         */
        setLayout(null)


        exitActionListener = ActionListener { exit() }

        exitButton = JButton("EXIT")
        exitButton.addActionListener(exitActionListener)
        this.add(exitButton)
    }


    protected fun exit() {
        LOGGER.info("exit action invoked...")
        this.isVisible = false
        parent.isVisible = false
        System.exit(0)
    }


    fun getTitle(): String {
        return title
    }


    fun setTitle(title: String) {
        /**
         * Assign argument to the class variable.
         */
        this.title = title

        /**
         * Remove the label from the panel if it already exists.
         */
        if (titleLabel != null) {
            this.remove(titleLabel)
        }

        /**
         * Get the width of the text string based the font metrics of the
         * LCARS font.
         */
        val fm: FontMetrics = getFontMetrics(
            LCARS.getSingletonObject().LCARSFont
                .deriveFont(LCARS.getLCARSFontSize(LCARS.EF_TITLE))
        )

        /**
         * Create the LCARS label object and add it to the panel. Note that
         * its location is set to the upper right of the panel.
         */
        titleLabel = LCARSLabel(
            title,
            PREFERRED_WIDTH - fm.stringWidth(title) - 100, 10,
            LCARS.EC_ORANGE or LCARS.ES_LABEL_NE or LCARS.EF_TITLE
        )
        this.add(titleLabel)
    }


    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARSPanel::class.java)

        const val PREFERRED_WIDTH: Int = 1920
        const val PREFERRED_HEIGHT: Int = 1080

        /**
         * The generated serial version identifier.
         */
        private const val serialVersionUID = -9217248917750616751L
    }
}
