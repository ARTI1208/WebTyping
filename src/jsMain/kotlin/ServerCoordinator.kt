import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.utils.*

object ServerCoordinator {

    val client = HttpClient(Js) {
        install(WebSockets)
        install(HttpTimeout)
    }

}