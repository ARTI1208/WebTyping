@file:JsModule("react-showdown")
@file:JsNonModule

package components.external.md

import react.*

external interface MarkdownViewProps : RProps {

    var markdown: String

}

@JsName("default")
external class MarkdownView : Component<MarkdownViewProps, RState> {

    override fun render(): dynamic
}