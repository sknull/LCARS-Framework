/**
 *
 */
package com.spagnola.lcars.elements;


import com.spagnola.lcars.LCARS;

import java.awt.FontMetrics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Perry Spagnola
 *
 */
public class LCARSTime extends LCARSLabel {

    public static final int DEFAULT = 0;
    public static final int DAY_OF_WEEK = 1;
    public static final int AM_PM = 2;
    public static final int DATE = 3;
    public static final int TIME_ZONE = 4;
    public static final int TIME_ZONE_RFC822 = 5;
    public static final int TIME = 6;
    public static final int SECONDS = 7;
    public static final int TIME_24 = 8;
    protected static final SimpleDateFormat[] formattedTime = {
            new SimpleDateFormat("HH:mm:ss"),
            new SimpleDateFormat("EEEE"),
            new SimpleDateFormat("a"),
            new SimpleDateFormat("MMMM d, yyyy"),
            new SimpleDateFormat("zzzz"),
            new SimpleDateFormat("Z"),
            new SimpleDateFormat("h:mm"),
            new SimpleDateFormat("ss"),
            new SimpleDateFormat("HH:mm")
    };
    protected static TimeZone timezone;
    protected SimpleDateFormat format;

    /**
     * @param parent
     * @param x
     * @param y
     * @param style
     */
    public LCARSTime(int x, int y, int style) {
        super("", x, y, style);

        format = formattedTime[DEFAULT];

        init();

    }

    protected void init() {
        componentText = format.format(Calendar.getInstance().getTime());
        timezone = Calendar.getInstance().getTimeZone();

        FontMetrics fm = getFontMetrics(LCARSFont.deriveFont(fontSize));
        w = fm.stringWidth(componentText);
        h = fm.getHeight();
        createLabelShape();

        setBounds(0, 0, w, h);

        setTextInsets(0, -10);
        //setForeground(LCARS.getLCARSColor(style));

        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                format.setTimeZone(timezone);
                setText(format.format(Calendar.getInstance().getTime()));
                repaint();
            }
        }, LCARS.SECOND, LCARS.SECOND);
    }


    public LCARSTime(int x, int y, int mode, int size, int style) {
        super("", x, y, style);

        fontSize = size;

        format = formattedTime[mode];

        init();

    }

    public static TimeZone getTimeZone() {
        return timezone;
    }

    public static void setTimeZone(String timezoneID) {
        timezone = TimeZone.getTimeZone(timezoneID);
    }

    public static void setTimeZone() {
        timezone = Calendar.getInstance().getTimeZone();
    }

    public void setMode(int mode) {
        format = formattedTime[mode];
    }
}
