package dr.shadcn.kmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dr.shadcn.kmp.MainRoute
import dr.shadcn.kmp.pages.components.AccordionPage
import dr.shadcn.kmp.pages.components.AlertDialogPage
import dr.shadcn.kmp.pages.components.AlertPage
import dr.shadcn.kmp.pages.components.AvatarPage
import dr.shadcn.kmp.pages.components.BadgePage
import dr.shadcn.kmp.pages.components.ButtonPage
import dr.shadcn.kmp.pages.components.CalendarPage
import dr.shadcn.kmp.pages.components.CardPage
import dr.shadcn.kmp.pages.components.CardPlaygroundPage
import dr.shadcn.kmp.pages.components.CarouselPage
import dr.shadcn.kmp.pages.components.CheckboxPage
import dr.shadcn.kmp.pages.components.ComboBoxPage
import dr.shadcn.kmp.pages.components.DatePickerPage
import dr.shadcn.kmp.pages.components.DropDownPage
import dr.shadcn.kmp.pages.components.InputPage
import dr.shadcn.kmp.pages.components.PopoverPage
import dr.shadcn.kmp.pages.components.ProgressPage
import dr.shadcn.kmp.pages.components.RadioButtonPage
import dr.shadcn.kmp.pages.components.SelectPage
import dr.shadcn.kmp.pages.components.SidebarCollectionPage
import dr.shadcn.kmp.pages.components.SkeletonPage
import dr.shadcn.kmp.pages.components.SliderPage
import dr.shadcn.kmp.pages.components.SonnerPage
import dr.shadcn.kmp.pages.components.SwitchPage
import dr.shadcn.kmp.pages.components.TabsPage
import dr.shadcn.kmp.pages.home.HomePage
import dr.shadcn.kmp.pages.theme.ThemesPage
import dr.shadcn.kmp.pages.theme.ThemesViewModel

@Composable
fun ComponentNavigation(
    parentNav: NavHostController,
    childNav: NavHostController,
    prefs: DataStore<Preferences>,
    modifier: Modifier,
) {
    NavHost(
        navController = childNav,
        startDestination = MainRoute.HomePage.path,
        modifier = modifier
    ) {
        composable(MainRoute.HomePage.path) {
            HomePage(parentNav)
        }
        composable(MainRoute.Button.path) {
            ButtonPage()
        }
        composable(MainRoute.Accordion.path) {
            AccordionPage()
        }
        composable(MainRoute.Alert.path) {
            AlertPage()
        }
        composable(MainRoute.Dialog.path) {
            AlertDialogPage()
        }
        composable(MainRoute.Avatar.path) {
            AvatarPage()
        }
        composable(MainRoute.Badge.path) {
            BadgePage()
        }
        composable(MainRoute.Calendar.path) {
            CalendarPage()
        }
        composable(MainRoute.Card.path) {
            CardPage(childNav)
        }
        composable(MainRoute.CardPlayground.path) {
            CardPlaygroundPage()
        }
        composable(MainRoute.Carousel.path) {
            CarouselPage()
        }
        composable(MainRoute.Checkbox.path) {
            CheckboxPage()
        }
        composable(MainRoute.ComboBox.path) {
            ComboBoxPage()
        }
        composable(MainRoute.DatePicker.path) {
            DatePickerPage()
        }
        composable(MainRoute.DropDown.path) {
            DropDownPage()
        }
        composable(MainRoute.Input.path) {
            InputPage()
        }
        composable(MainRoute.Popover.path) {
            PopoverPage()
        }
        composable(MainRoute.Progress.path) {
            ProgressPage()
        }
        composable(MainRoute.RadioButton.path) {
            RadioButtonPage()
        }
        composable(MainRoute.Select.path) {
            SelectPage()
        }
        composable(MainRoute.Sidebar.path) {
            SidebarCollectionPage(parentNav)
        }
        composable(MainRoute.Sonner.path) {
            SonnerPage()
        }
        composable(MainRoute.Skeleton.path) {
            SkeletonPage()
        }
        composable(MainRoute.Slider.path) {
            SliderPage()
        }
        composable(MainRoute.Switch.path) {
            SwitchPage()
        }
        composable(MainRoute.Tabs.path) {
            TabsPage()
        }
        composable(MainRoute.Themes.path) {
            ThemesPage(viewModel<ThemesViewModel>(factory = viewModelFactory {
                initializer {
                    ThemesViewModel(
                        prefs
                    )
                }
            }))
        }
    }
}