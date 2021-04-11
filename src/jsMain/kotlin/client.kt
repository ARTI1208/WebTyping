import components.Typing
import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    window.onload = {

        render(document.getElementById(PageSetup.clientRenderElementId)) {
            child(Typing::class) {}
        }
    }
}
