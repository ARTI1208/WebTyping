import io.ktor.application.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.websocket.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Typing")
    }
    body {
        div {
            id = PageSetup.clientRenderElementId
        }
        script(src = "/static/js.js") {}
    }
}

fun main() {

    embeddedServer(Netty, port = ServerSetup.port, host = ServerSetup.host) {
        install(WebSockets)
        routing {

            webSocket(WebSocketSetup.typeLoadPath) {
                send(ServerState.text)
            }

            webSocket(WebSocketSetup.typePath) {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        ServerState.text = frame.readText()
                        send(ServerState.text)
                    }
                }
            }

            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}