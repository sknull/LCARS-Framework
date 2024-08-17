package com.spagnola.lcars

import com.spagnola.lcars.elements.LCARSButton
import com.spagnola.lcars.elements.LCARSCalendarPane
import com.spagnola.lcars.elements.LCARSCorner
import com.spagnola.lcars.elements.LCARSRectangle
import com.spagnola.lcars.elements.LCARSTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JPanel

object ComponentTest {
    private val LOGGER: Logger = LoggerFactory.getLogger(ComponentTest::class.java)


    @JvmStatic
    fun main(args: Array<String>) {
        val f = JFrame("LCARS Component Test")
        f.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                System.exit(0)
            }
        })

        val panel = JPanel()
        panel.preferredSize = Dimension(1920, 1080)
        panel.layout = null
        panel.background = Color.black

        val lt = LCARSTime(
            500, 700, LCARSTime.TIME, 100,
            LCARS.EF_TITLE or LCARS.EC_ORANGE or LCARS.ES_LABEL_C
        )
        panel.add(lt)

        lt.setMode(LCARSTime.DEFAULT)

        var lc = LCARSCorner(
            x = 10, y = 10, w = 1490,
            style = LCARS.EC_ORANGE or LCARS.ES_SHAPE_NW or LCARS.ES_LABEL_NE or LCARS.ES_STATIC
        )
        lc.setText("Login Panel Header")
        lc.name = "Login Panel Header"


        panel.add(lc)

        lc = LCARSCorner(
            x = 10, y = 200, w = 1490,
            style = LCARS.EC_BLUE or LCARS.ES_SHAPE_SW or LCARS.ES_LABEL_NE or LCARS.ES_STATIC
        )
        lc.setText("Login Panel 2")
        lc.name = "Login Panel 2"
        panel.add(lc)

        lc = LCARSCorner(
            x = 10, y = 400, w = 1490,
            style = LCARS.EC_D_BLUE or LCARS.ES_SHAPE_NE or LCARS.ES_LABEL_NE or LCARS.ES_STATIC
        )
        lc.setText("Login Panel 3")
        lc.name = "Login Panel 3"
        panel.add(lc)

        lc = LCARSCorner(
            x = 10, y = 600, w = 1490,
            style = LCARS.EC_L_BLUE or LCARS.ES_SHAPE_SE or LCARS.ES_LABEL_NE or LCARS.ES_STATIC
        )
        lc.setText("Login Panel 4")
        lc.name = "Login Panel 4"
        panel.add(lc)


        var lr = LCARSRectangle(x = 200, y = 50, w = 200, h = 100, style = LCARS.ES_RECT_RND or LCARS.ES_STATIC)
        panel.add(lr)

        lr = LCARSRectangle(x = 500, y = 50, w = 200, h = 100, style = LCARS.ES_RECT_RND_E or LCARS.ES_STATIC)
        panel.add(lr)

        lr = LCARSRectangle(x = 800, y = 50, w = 200, h = 100, style = LCARS.ES_RECT_RND_W or LCARS.ES_STATIC)
        panel.add(lr)

        lr = LCARSRectangle(x = 1100, y = 50, w = 200, h = 100, style = LCARS.ES_STATIC)
        panel.add(lr)

        var lb = LCARSButton(
            text = "Test Button", x = 200, y = 450,
            style = LCARS.EF_BUTTON or LCARS.ES_RECT_RND or LCARS.EC_ORANGE
        )
        lb.name = "Test Button"
        panel.add(lb)

        lb = LCARSButton(
            text = "Test Button 2", x = 500, y = 450,
            style = LCARS.EF_BUTTON or LCARS.ES_RECT_RND_W or LCARS.EC_ORANGE
        )
        lb.name = "Test Button 2"
        panel.add(lb)

        lb = LCARSButton(
            text = "Test Button 3", x = 800, y = 450,
            style = LCARS.EF_BUTTON or LCARS.ES_RECT_RND_E or LCARS.EC_ORANGE
        )
        lb.name = "Test Button 3"
        panel.add(lb)

        lb = LCARSButton(
            text = "Test Button 4", x = 1100, y = 450,
            style = LCARS.EF_BUTTON or LCARS.EC_ORANGE
        )
        lb.name = "Test Button 4"
        panel.add(lb)


        //        LCARSLabel titleLabel = new LCARSLabel("LCARS Label",
//                1510, 10, 
//                LCARS.EC_ORANGE|LCARS.ES_LABEL_NW|LCARS.EF_TITLE);
//        titleLabel.setName("LCARS Label");        
//        panel.add(titleLabel);
//        
//        titleLabel.setText("LCARS Long Label");

//        LCARSTextPane ltp = new LCARSTextPane(100, 550, 200, 100, LCARS.EC_BLUE | LCARS.EF_BODY);
//        ltp.setText("Line 2\n");
//        ltp.appendText("Line 3\n");
//        ltp.prependText("Line 1\n");
//
//        panel.add(ltp);
        val lcp = LCARSCalendarPane(x = 20, y = 700, style = LCARS.EF_BODY)
        panel.add(lcp)

        //        lcp = new LCARSCalendarPane(850, 700, 11, 2013, LCARS.EF_BODY);
//        panel.add(lcp);
        f.contentPane.add(panel)


        f.size = Dimension(1200, 800)
        f.isVisible = true
    }
}
