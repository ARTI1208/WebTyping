package components.external.md

import react.RBuilder
import react.RElementBuilder
import react.ReactElement
import kotlin.js.Promise

fun RBuilder.reactMde(props: RElementBuilder<ReactMdeProps>.() -> Unit) =
    child(ReactMde::class, props)

typealias GenerateMarkdownPreview = (String) -> Promise<ReactElement>

fun RBuilder.markdownView(props: RElementBuilder<MarkdownViewProps>.() -> Unit) =
    child(MarkdownView::class, props)

fun ReactMdeProps.setMarkdownPreviewGenerator(gen: (String) -> ReactElement) {
    generateMarkdownPreview = { md -> Promise.resolve(gen(md)) }
}