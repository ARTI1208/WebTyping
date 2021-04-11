package components

import ServerCoordinator
import ServerSetup
import WebSocketSetup
import components.external.md.markdownView
import components.external.md.reactMde
import components.external.md.setMarkdownPreviewGenerator
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinext.js.require
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.css.RuleSet
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styles.TypingStyles

external interface TypingProps : RProps {
    var timeout: Long?
}

data class TypingState(
    var text: String,
    val timeout: Long,
    var receivedText: Boolean = false,
    var responseMatches: Boolean = true,
    var preview: Boolean = false,
    var loading: Boolean = false
) : RState

class Typing(props: TypingProps) : RComponent<TypingProps, TypingState>(props) {

    init {
        state = TypingState("", props.timeout ?: 3000, loading = false, receivedText = true)
        require("react-mde/lib/styles/css/react-mde-all.css")

        GlobalScope.launch {
            ServerCoordinator.client.ws(
                host = ServerSetup.host,
                port = ServerSetup.port,
                path = WebSocketSetup.typeLoadPath
            ) {

                setState {
                    loading = true
                    receivedText = false
                }

                val frame = withTimeoutOrNull(state.timeout) { incoming.receive() }

                if (frame is Frame.Text) {

                    val text = frame.readText()

                    setState {
                        this.text = text
                        receivedText = true
                        loading = false
                    }

                    return@ws
                }

                setState {
                    receivedText = false
                    loading = false
                }
            }
        }
    }

    override fun RBuilder.render() {

        reactMde {

            attrs {
                value = state.text
                onChange = { newText ->
                    setState {
                        text = newText
                        receivedText = true
                    }
                    sendText(newText)
                }
                selectedTab = if (state.preview) "preview" else "write"
                onTabChange = { tab -> setState { preview = tab == "preview" } }
                setMarkdownPreviewGenerator {
                    markdownView {
                        attrs { markdown = it }
                    }
                }
            }
        }


        if (state.loading) {
            val text = if (state.receivedText) "Sending to server..." else "Loading initial text..."
            box(text, TypingStyles.loadingBox)
        } else if (!state.receivedText) {
            val text = "Server text not loaded"
            box(text, TypingStyles.errorBox)
        } else if (!state.responseMatches) {
            val text = "Mismatch of local and server text! Showing server text.."
            box(text, TypingStyles.errorBox)
        }

//        if (!state.receivedText) {
//            val text = if (state.loading) "Loading initial text..." else "Server text not loaded"
//            val style = if (state.loading) TypingStyles.loadingBox else TypingStyles.errorBox
//            box(text, style)
//        }
//
//        if (!state.responseMatches) {
//            val text =
//                if (state.loading) "Sending to server..." else "Mismatch of local and server text! Showing server version"
//            val style = if (state.loading) TypingStyles.loadingBox else TypingStyles.errorBox
//            box(text, style)
//        }
    }

    private fun sendText(text: String) {
        GlobalScope.launch {
            ServerCoordinator.client.ws(
                host = ServerSetup.host,
                port = ServerSetup.port,
                path = WebSocketSetup.typePath,
            ) {
                setState { loading = true }
                send(text)

                val response = withTimeoutOrNull(state.timeout) { incoming.receive() }

                val (newText, received) = if (response is Frame.Text) {
                    val responseText = response.readText()
                    responseText to true
                } else {
                    text to false
                }

                setState {
                    this.text = newText
                    responseMatches = (text == newText)
                    loading = false
                    receivedText = received
                }
            }
        }
    }

    private fun <T : RState> RComponent<*, T>.setState(update: T.() -> Unit) {
        setState({
            update(it)
            it
        })
    }

    private fun RBuilder.box(text: String, style: RuleSet) {
        styledDiv {
            css { +style }

            +text
        }
    }

}
