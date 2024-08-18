package de.visualdigits.lcars.bufferedimage.components

import de.visualdigits.lcars.bufferedimage.type.LabelPosition
import de.visualdigits.lcars.bufferedimage.type.NamedColor
import de.visualdigits.lcars.bufferedimage.type.Orientation
import de.visualdigits.lcars.bufferedimage.type.Style
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class LCARSImageComponentTest {

    @Test
    fun testCorner() {

        val image = BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB)

        val corner = LCARSImageCorner(
            w = 1900,
            h = 1060,
            style = Style(
                background = Color.black,
                foreground = NamedColor.YELLOW.color,
                orientation = Orientation.BOTTOM_RIGHT,
                labelPosition = LabelPosition.TOP_RIGHT
            ),
            text = "hello"
        ).draw()

        image.createGraphics().drawImage(corner, 10, 10, null)

        ImageIO.write(image, "png", File("c:/tmp/LCARS-Test.png"))
    }
}
