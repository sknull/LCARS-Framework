package de.visualdigits.lcars.panels

import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class LMainPanel(logoutAction: ActionListener?) : JPanel() {
    private var logoutButton: JButton

    init {
        val label: JLabel = JLabel("Main Panel")
        this.add(label)

        logoutButton = JButton("LOGOUT")
        logoutButton.addActionListener(logoutAction)
        this.add(logoutButton)
    }
}
