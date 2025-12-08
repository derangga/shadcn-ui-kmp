package dr.shadcn.kmp

enum class WebViewSlug {
    Github,
    Documentation,
}
sealed class TopLevelRoute(val path: String) {
    object MainGraph : TopLevelRoute("main")
    object SidebarLayoutGraph : TopLevelRoute("sidebar_layout")
    object SidebarInsetGraph : TopLevelRoute("sidebar_inset")
    object WebviewGraph : TopLevelRoute("webview/{slug}") {
        fun pathWithSlug(slug: WebViewSlug) = "webview/${slug.name}"
    }
}

sealed class MainRoute(val path: String) {
    data object HomePage : MainRoute("home")
    data object Button : MainRoute("button")
    data object Accordion : MainRoute("accordion")
    data object Alert : MainRoute("alert")
    data object Dialog : MainRoute("dialog")
    data object Avatar : MainRoute("avatar")
    data object Badge : MainRoute("badge")
    data object Calendar : MainRoute("calendar")
    data object Card : MainRoute("card")
    data object CardPlayground : MainRoute("card-playground")
    data object Carousel : MainRoute("carousel")
    data object Chart : MainRoute("chart")
    data object Checkbox : MainRoute("checkbox")
    data object ComboBox : MainRoute("combobox")
    data object DatePicker : MainRoute("date-picker")
    data object DropDown : MainRoute("dropdown")
    data object Input : MainRoute("input")
    data object Popover : MainRoute("popover")
    data object Progress : MainRoute("progress")
    data object RadioButton : MainRoute("radio-button")
    data object Select : MainRoute("select")
    data object Sidebar : MainRoute("sidebar")
    data object Sonner : MainRoute("sonner")
    data object Skeleton : MainRoute("skeleton")
    data object Slider : MainRoute("slider")
    data object Switch : MainRoute("switch")
    data object Tabs : MainRoute("tabs")
    data object Themes : MainRoute("themes-selection")
}

sealed class SidebarRoute(val path: String) {
    data object Dashboard : MainRoute("dashboard")
    data object Project : MainRoute("project")
    data object Task : MainRoute("task")
}
