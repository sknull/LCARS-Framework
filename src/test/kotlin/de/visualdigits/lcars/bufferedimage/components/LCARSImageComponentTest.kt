package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.NamedColor
import de.visualdigits.lcars.bufferedimage.type.Orientation
import de.visualdigits.lcars.bufferedimage.type.Style
import org.junit.jupiter.api.Test
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class LCARSImageComponentTest {

    @Test
    fun testCorner() {

        val image = BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB)

        val corner1 = LCARSImageCorner(
            w = 1900,
            h = 1060,
            style = Style(
                foreground = NamedColor.YELLOW.color,
                labelColor = Color.black,
                labelPosition = LabelPosition.TOP_RIGHT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.TOP_LEFT
            ),
            barV = 50,
            barH = 200,
            text = "TERMINAL 4711-13"
        ).draw()

        val corner2 = LCARSImageCorner(
            w = corner1.width - corner1.barH - 10,
            h = corner1.height - corner1.barV - 10,
            style = Style(
                foreground = NamedColor.RED.color,
                labelColor = Color.white,
                labelPosition = LabelPosition.TOP_RIGHT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.TOP_LEFT
            ),
            barV = 50,
            barH = 200,
            text = "CONSOLE INPUT",
            roundingFactor = 1.0
        ).draw()

        val subwindowW = ((corner2.width - corner1.barH - 10) / 2.0 - 5).roundToInt()
        val subwindowH = ((corner2.height - corner1.barV - 10) / 2.0 - 5).roundToInt()

        val corner3 = LCARSImageCorner(
            w = subwindowW,
            h = subwindowH,
            style = Style(
                foreground = NamedColor.BLUE.color,
                labelColor = Color.white,
                labelPosition = LabelPosition.BOTTOM_LEFT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.BOTTOM_RIGHT
            ),
            barV = 20,
            barH = 100,
            text = "DATA MONITOR 1",
        ).draw()

        val corner4 = LCARSImageCorner(
            w = subwindowW,
            h = subwindowH,
            style = Style(
                foreground = NamedColor.ORANGE.color,
                labelColor = Color.white,
                labelPosition = LabelPosition.BOTTOM_LEFT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.BOTTOM_RIGHT
            ),
            barV = 20,
            barH = 100,
            text = "DATA MONITOR 2",
        ).draw()

        val corner5 = LCARSImageCorner(
            w = subwindowW,
            h = subwindowH,
            style = Style(
                foreground = NamedColor.DARK_BLUE.color,
                labelColor = Color.white,
                labelPosition = LabelPosition.BOTTOM_LEFT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.BOTTOM_RIGHT
            ),
            barV = 20,
            barH = 100,
            text = "DATA MONITOR 3",
        ).draw()

        val corner6 = LCARSImageCorner(
            w = subwindowW,
            h = subwindowH,
            style = Style(
                foreground = NamedColor.LIGHT_BLUE.color,
                labelColor = Color.white,
                labelPosition = LabelPosition.BOTTOM_LEFT,
                labelSize = 10,
                labelInsetX = 15,
                orientation = Orientation.BOTTOM_RIGHT
            ),
            barV = 20,
            barH = 100,
            text = "DATA MONITOR 4",
        ).draw()

        val g = image.createGraphics()
        g.composite = AlphaComposite.SrcOver
        g.drawImage(corner1, 10, 10, null)
        g.drawImage(corner2, 10 + corner1.barH + 10, 10 + corner1.barV + 10, null)

        val subwindowX = 10 + corner1.barH + 10 + corner2.barH + 10
        val subwindowY = 10 + corner1.barV + 10 + corner2.barV + 10
        g.drawImage(corner3, subwindowX, subwindowY, null)
        g.drawImage(corner4, subwindowX + subwindowW + 5, subwindowY, null)
        g.drawImage(corner5, subwindowX, subwindowY + subwindowH + 5, null)
        g.drawImage(corner6, subwindowX + subwindowW + 5, subwindowY + subwindowH + 5, null)

        ImageIO.write(image, "png", File("c:/tmp/LCARS-Test.png"))
    }

    @Test
    fun testTransparency() {
        val image = BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)
        ImageIO.write(image, "png", File("c:/tmp/Transparency-Test.png"))
    }
}
