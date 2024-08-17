package de.visualdigits.lcars

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Font
import java.awt.FontFormatException
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URISyntaxException
import java.net.UnknownHostException
import java.util.Enumeration

class LCARS private constructor() {

    lateinit var LCARSFont: Font

    /**
     * The default constructor for the LCARS class. Instantiates an LCARS object and loads
     * the LCARS font.
     */
    init {
        /**
         * Load the LCARS Font.
         */
        loadLCARSFont()
    }

    /**
     * Load the LCARS font from a resource in the classpath.
     */
    fun loadLCARSFont() {
        try {
            /**
             * Try to load the LCARS font.
             */
            LCARSFont =
                Font.createFont(Font.TRUETYPE_FONT, File(ClassLoader.getSystemResource(LCARS_FONT_FILENAME).toURI()))
        } catch (e: IOException) {
            /**
             * Log IOException, FontFormatException, and URISyntaxException if thrown.
             */
            LOGGER.error("Error loading LCARS font: ", e)
        } catch (e: FontFormatException) {
            LOGGER.error("Error loading LCARS font: ", e)
        } catch (e: URISyntaxException) {
            LOGGER.error("Error loading LCARS font: ", e)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LCARS::class.java)

        // 
        // ES_XXX - Element styles
        // 
        const val ES_SHAPE: Int = 0x0000000F // Mask for ES_SHAPE_XXX styles
        const val ES_LABEL: Int = 0x000000F0 // Mask for ES_LABEL_XXX styles
        const val ES_STYLE: Int = 0x0000FC00 // Mask for color style
        const val ES_COLOR: Int = 0x0000FF00 // Mask for EC_XXX styles
        const val ES_FONT: Int = 0x000F0000 // Mask for EF_XXX styles
        const val ES_BEHAVIOR: Int = 0x0F000000 // Mask for EB_XXX styles
        const val ES_CLASS: Int = -0x10000000 // Mask for class specific styles
        const val ES_SELECTED: Int = 0x00000100 // Element selected
        const val ES_DISABLED: Int = 0x00000200 // Element disabled
        const val ES_SELDISED: Int = 0x00000300 // Element selected and disabled
        const val ES_STATIC: Int = 0x00100000 // Element does not accept user input
        const val ES_BLINKING: Int = 0x00200000 // Element blinks
        const val ES_MODAL: Int = 0x00400000 // Element is always opaque
        const val ES_SILENT: Int = 0x00800000 // Element does not play a sound
        const val ES_NONE: Int = 0x00000000 // Element does not have a style

        //
        // ES_SHAPE_XXX - Element shape orientation
        // 
        const val ES_SHAPE_NW: Int = 0x00000000 // Shape oriented to the north-west
        const val ES_SHAPE_SW: Int = 0x00000001 // Shape oriented to the south-west
        const val ES_SHAPE_NE: Int = 0x00000002 // Shape oriented to the north-east
        const val ES_SHAPE_SE: Int = 0x00000004 // Shape oriented to the south-east
        const val ES_OUTLINE: Int = 0x00000008 // Outline

        // 
        // ES_LABEL_XXX - Element label position
        // 
        const val ES_LABEL_SE: Int = 0x00000000 // Label in the south-east - the default for LCARS components
        const val ES_LABEL_S: Int = 0x00000010 // Label in the south
        const val ES_LABEL_SW: Int = 0x00000020 // Label in the south-west
        const val ES_LABEL_W: Int = 0x00000030 // Label in the west
        const val ES_LABEL_NW: Int = 0x00000040 // Label in the north-west
        const val ES_LABEL_N: Int = 0x00000050 // Label in the north
        const val ES_LABEL_NE: Int = 0x00000060 // Label in the north-east
        const val ES_LABEL_E: Int = 0x00000070 // Label in the east
        const val ES_LABEL_C: Int = 0x00000080 // Label in the center

        // 
        //  ES_RECT_XXX - Rectangle/Button element styles
        // 
        const val ES_RECT_RND: Int = 0x30000000 // Rounded rectangle shape
        const val ES_RECT_RND_E: Int = 0x10000000 // Rounded rectangle shape in the east
        const val ES_RECT_RND_W: Int = 0x20000000 // Rounded rectangle shape in the west

        // 
        // EC_XXX - Colors by Name
        // 
        const val EC_WHITE: Int = 0x00000000 //
        const val EC_L_BLUE: Int = 0x00000400 //
        const val EC_M_BLUE: Int = 0x00000800 //
        const val EC_BLUE: Int = 0x00000C00 //
        const val EC_D_BLUE: Int = 0x00001000 //
        const val EC_YELLOW: Int = 0x00001400 //
        const val EC_ORANGE: Int = 0x00001800
        const val EC_RED: Int = 0x00001C00

        val COLORS_BG: Array<Color> = arrayOf<Color>(
            Color(0xCC, 0xDD, 0xFF),
            Color(0x55, 0x99, 0xFF),
            Color(0x33, 0x66, 0xFF),
            Color(0x00, 0x11, 0xEE),
            Color(0x00, 0x00, 0x88),
            Color(0xBB, 0xAA, 0x55),
            Color(0xBB, 0x44, 0x11),
            Color(0x88, 0x22, 0x11)
        )

        // EF_XXX - Fonts
        const val EF_NORMAL: Int = 0x00000000 // The normal LCARS font
        const val EF_TITLE: Int = 0x00010000 // The title font
        const val EF_SUBTITLE: Int = 0x00020000 // The subtitle font
        const val EF_BUTTON: Int = 0x00030000 // The default button text font
        const val EF_BODY: Int = 0x00040000 // The default body text font
        const val EF_TINY: Int = 0x00050000 // A tiny font

        const val FONT_TITLE_SIZE: Float = 60f
        const val FONT_SUBTITLE_SIZE: Float = 40f
        const val FONT_BUTTON_SIZE: Float = 25f
        const val FONT_BODY_SIZE: Float = 20f
        const val FONT_TINY_SIZE: Float = 10f

        const val FN_SWISS911: String = "Swiss911 UCm BT"

        /**
         * Time constant for second in miliseconds.
         */
        const val SECOND: Int = 1000

        /**
         * Time constant for minute in miliseconds.
         */
        const val MINUTE: Int = 60000

        /**
         * Time constans for miliseconds in an hour.
         */
        const val MILISECS_IN_HOUR: Int = 3600000

        /**
         * A reference to the global Logger object.
         */
        private const val LCARS_FONT_FILENAME = "swz911uc.ttf"
        private var reference: LCARS? = null

        /**
         * Cache for the [.getHostName] method.
         */
        private var hostName: String? = null

        fun getHostName(): String {
            if (hostName == null) {
                hostName = try {
                    InetAddress.getLocalHost().hostName
                } catch (e: UnknownHostException) {
                    "localhost"
                }
            }

            return hostName!!
        }

        /**
         * Cache for the [.getIPAddress] method.
         */
        private var ipAddress: String? = null


        /**
         * Get the singleton object for the LCARS class. It creates the object if it does
         * not already exist.
         *
         * @return a reference to the singleton LCARS object
         */
        fun getSingletonObject(): LCARS {
            /**
             * If the singleton object reference is `null` it hasn't been
             * created, yet. So, create it using the default constructor.
             */
            if (reference == null) {
                reference = LCARS()
            }

            /**
             * Return a reference to this singleton object.
             */
            return reference!!
        }

        fun getLCARSFontSize(style: Int): Float {
            val fontSizeID = style and ES_FONT

            return when (fontSizeID) {
                EF_TITLE -> FONT_TITLE_SIZE
                EF_SUBTITLE -> FONT_SUBTITLE_SIZE
                EF_BUTTON -> FONT_BUTTON_SIZE
                EF_BODY -> FONT_BODY_SIZE
                else -> 0f
            }
        }

        /**
         * Returns the color derived from the LCARS component style passed
         * in as an argument.
         *
         * @param componentStyle the style of an LCARS component
         * @return the color derived from the component's style
         */
        fun getLCARSColor(componentStyle: Int): Color {
            /**
             * Derive the color array index from the component style.
             */

            val colorIndex = (componentStyle and ES_COLOR) shr 10

            /**
             * If the index into the colors array is valid, use it to
             * retrieve the assigned color and return it. Else, return
             * `Color.lightGray`.
             */
            return if (colorIndex >= 0 && colorIndex < COLORS_BG.size) {
                COLORS_BG[colorIndex]
            } else {
                Color.lightGray
            }
        }

        fun getLCARSTextColor(componentStyle: Int): Color {
            /**
             * Derive the color array index from the component style.
             */
            val colorIndex = (componentStyle and ES_COLOR) shr 10

            return if (colorIndex == 3 || colorIndex == 4) {
                COLORS_BG[0]
            } else {
                Color.black
            }
        }

        fun getShape(style: Int): Int {
            return style and ES_SHAPE and ES_OUTLINE.inv()
        }

        fun isStatic(style: Int): Boolean {
            return (style and ES_STATIC) != 0
        }

        fun getRectangleShape(style: Int): Int {
            return style and ES_RECT_RND
        }

        fun getIPAddress(): String? {
            if (ipAddress == null) {
                ipAddress = "127.0.0.1"
                try {
                    var ipAddress: InetAddress? = null

                    val netInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                    while (netInterfaces.hasMoreElements()) {
                        val inetAddresses: Enumeration<InetAddress> = netInterfaces.nextElement().inetAddresses
                        while (inetAddresses.hasMoreElements()) {
                            val ip: InetAddress = inetAddresses.nextElement()
                            if (ipAddress == null && !ip.isLoopbackAddress && ip.hostAddress
                                    .indexOf(":") === -1) {
                                ipAddress = ip
                            }
                        }
                    }
                    if (ipAddress != null) {
                        Companion.ipAddress = ipAddress.hostAddress
                    } else {
                        throw UnknownHostException()
                    }
                } catch (uhe: UnknownHostException) {
                    LOGGER.warn(uhe.message + " - Couldn't find IP address, returning local loop back address.")
                } catch (se: SocketException) {
                    LOGGER.warn(se.message + " - Socket exception while trying to get network interfaces.")
                }
            }

            return ipAddress
        }
    }
}
