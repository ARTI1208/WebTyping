package styles

import kotlinx.css.*
import kotlinx.css.properties.boxShadow
import styled.StyleSheet

object TypingStyles : StyleSheet("TypingStyles", true) {

    private val baseBox by css {
        borderRadius = 5.px
        borderColor = Color.white
        borderWidth = 1.px
        fontWeight = FontWeight.bold
        fontSize = 20.px
        boxShadow(Color.gray, blurRadius = 2.px)
        margin(vertical = 10.px)
        padding(5.px)
    }

    val errorBox by css(baseBox) {
        backgroundColor = Color.red
        color = Color.white
    }

    val loadingBox by css(baseBox) {
        backgroundColor = Color.yellow
        color = Color.black
    }

}