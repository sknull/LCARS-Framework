package com.spagnola.lcars;

import com.spagnola.lcars.elements.LCARSCorner;
import com.spagnola.lcars.elements.LCARSPanel;
import com.spagnola.lcars.panels.LClockPanel;
import com.spagnola.lcars.panels.LoginPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class ClockTest extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClockTest.class);

    LoginPanel loginPanel;
    ActionListener loginAction;
    ActionListener logoutAction;

    public ClockTest() {

        /**
         * Set up the frame as maximized and undecorated with a title of "LCARS Main",
         * and EXIT_ON_CLOSE as the default close operation.
         */
        this.getContentPane().setBackground(Color.BLACK);
        this.setTitle("LCARS Clock Test");
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        /**
         * Create the LCARS clock panel to test.
         */
        LClockPanel lcp = new LClockPanel();
        lcp.setPreferredSize(new Dimension(LCARSPanel.PREFERRED_WIDTH, LCARSPanel.PREFERRED_HEIGHT));
        lcp.setTitle("LCARS Clock");
        this.getContentPane().add(lcp);


        validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClockTest();
            }
        });
    }
}
