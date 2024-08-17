package de.visualdigits.lcars.panels

import de.visualdigits.lcars.LCARS
import de.visualdigits.lcars.elements.LCARSCorner
import de.visualdigits.lcars.elements.LCARSPanel

class LoginPanel : LCARSPanel() {

    /**
     * @param parent
     * @param loginAction
     */
    init {
        this.setTitle("Login Title")

        val header: LCARSCorner = LCARSCorner(
            x = 10, y = 10, w = 1490, h = 200,
            barH = 75, barV = 400,
            style = LCARS.EC_WHITE or LCARS.ES_SHAPE_NW or LCARS.ES_LABEL_N or LCARS.ES_STATIC
        )
        header.setName("Login Header")
        header.componentText = header.getName()

        //header.actionListener = ActionListener(exitActionListener);
        //this.add(header);
        val footerLeft = LCARSCorner(
            x = 10, y = 980, w = 450, h = 100,
            barH = 40, barV = 400,
            style = LCARS.EC_WHITE or LCARS.ES_SHAPE_SW or LCARS.ES_LABEL_NW or LCARS.ES_STATIC
        )
        footerLeft.setName("Login Footer Left")
        footerLeft.componentText = footerLeft.getName()


        //this.add(footerLeft);
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = -4009903437795142762L
    }
}
