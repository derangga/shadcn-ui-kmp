package dr.shadcn.kmp

data class Content(
    val title: String,
    val route: String
)
object HomeContent {
    val contents = listOf(
        Content("Accordion", MainRoute.Accordion.path),
        Content("Alert", MainRoute.Alert.path),
        Content("Button", MainRoute.Button.path),
        Content("Avatar", MainRoute.Avatar.path),
        Content("Alert Dialog, Dialog, Drawer", MainRoute.Dialog.path),
        Content("Badge", MainRoute.Badge.path),
        Content("Calendar", MainRoute.Calendar.path),
        Content("Card", MainRoute.Card.path),
        Content("Carousel", MainRoute.Carousel.path),
        Content("Checkbox", MainRoute.Checkbox.path),
        Content("Combobox", MainRoute.ComboBox.path),
        Content("Date Picker", MainRoute.DatePicker.path),
        Content("Dropdown", MainRoute.DropDown.path),
        Content("Input", MainRoute.Input.path),
        Content("Popover", MainRoute.Popover.path),
        Content("Progress", MainRoute.Progress.path),
        Content("Radio Button", MainRoute.RadioButton.path),
        Content("Select", MainRoute.Select.path),
        Content("Sidebar", MainRoute.Sidebar.path),
        Content("Sonner", MainRoute.Sonner.path),
        Content("Skeleton", MainRoute.Skeleton.path),
        Content("Slider", MainRoute.Slider.path),
        Content("Switch", MainRoute.Switch.path),
        Content("Tabs", MainRoute.Tabs.path),
    )
}