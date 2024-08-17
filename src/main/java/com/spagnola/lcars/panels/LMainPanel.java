package com.spagnola.lcars.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;

public class LMainPanel extends JPanel {

    protected JButton logoutButton;

    public LMainPanel(ActionListener logoutAction) {
        JLabel label = new JLabel("Main Panel");
        this.add(label);

        logoutButton = new JButton("LOGOUT");
        logoutButton.addActionListener(logoutAction);
        this.add(logoutButton);


    }


}
