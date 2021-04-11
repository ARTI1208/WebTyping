@file:JsModule("react-mde")
@file:JsNonModule

package components.external.md

import react.*

external interface ReactMdeProps : RProps {

    var value: String
    var onChange: (String) -> Unit
    var selectedTab: String
    var onTabChange: (String) -> Unit
    var generateMarkdownPreview: GenerateMarkdownPreview
    var disablePreview: Boolean

//    minEditorHeight?: number;
//    maxEditorHeight?: number;
//    initialEditorHeight?: number;
//    minPreviewHeight?: number;
//    heightUnits?: string;
//    classes?: Classes;
//    refs?: Refs;
//    toolbarCommands?: ToolbarCommands;
//    commands?: CommandMap;
//    getIcon?: GetIcon;
//    loadingPreview?: React.ReactNode;
//    readOnly?: boolean;
//    disablePreview?: boolean;
//    suggestionTriggerCharacters?: string[];
//    suggestionsAutoplace?: boolean;
//    loadSuggestions?: (
//    text: string,
//    triggeredBy: string
//    ) => Promise<Suggestion[]>;
//    childProps?: ChildProps;
//    paste?: PasteOptions;
//    l18n?: L18n;
//    /**
//     * Custom textarea component. "textAreaComponent" can be any React component which
//     * props are a subset of the props of an HTMLTextAreaElement
//     */
//    textAreaComponent?: ComponentSimilarTo<
//    HTMLTextAreaElement,
//    TextareaHTMLAttributes<HTMLTextAreaElement>
//    >;
//    /**
//     * Custom toolbar button component. "toolbarButtonComponent" can be any React component which
//     * props are a subset of the props of an HTMLButtonElement
//     */
//    toolbarButtonComponent?: ComponentSimilarTo<
//    HTMLButtonElement,
//    ButtonHTMLAttributes<HTMLButtonElement>
//    >;
}

@JsName("default")
external class ReactMde : Component<ReactMdeProps, RState> {

    override fun render(): dynamic
}