package com.komoui.demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.komoui.demo.MainRoute
import com.komoui.demo.pages.components.AccordionPage
import com.komoui.demo.pages.components.AlertDialogPage
import com.komoui.demo.pages.components.AlertPage
import com.komoui.demo.pages.components.AvatarPage
import com.komoui.demo.pages.components.BadgePage
import com.komoui.demo.pages.components.ButtonPage
import com.komoui.demo.pages.components.CalendarPage
import com.komoui.demo.pages.components.CardPage
import com.komoui.demo.pages.components.CardPlaygroundPage
import com.komoui.demo.pages.components.CarouselPage
import com.komoui.demo.pages.components.ChartPage
import com.komoui.demo.pages.components.CheckboxPage
import com.komoui.demo.pages.components.CollapsiblePage
import com.komoui.demo.pages.components.ComboBoxPage
import com.komoui.demo.pages.components.DataTablePage
import com.komoui.demo.pages.components.DatePickerPage
import com.komoui.demo.pages.components.DropDownPage
import com.komoui.demo.pages.components.EmptyPage
import com.komoui.demo.pages.components.InputOTPPage
import com.komoui.demo.pages.components.InputPage
import com.komoui.demo.pages.components.PopoverPage
import com.komoui.demo.pages.components.ProgressPage
import com.komoui.demo.pages.components.RadioButtonPage
import com.komoui.demo.pages.components.SelectPage
import com.komoui.demo.pages.components.SidebarCollectionPage
import com.komoui.demo.pages.components.SkeletonPage
import com.komoui.demo.pages.components.SpinnerPage
import com.komoui.demo.pages.components.SliderPage
import com.komoui.demo.pages.components.SonnerPage
import com.komoui.demo.pages.components.SwitchPage
import com.komoui.demo.pages.components.TabsPage
import com.komoui.demo.pages.home.HomePage
import com.komoui.demo.pages.theme.ThemesPage
import com.komoui.demo.pages.theme.ThemesViewModel
import com.komoui.demo.themes.AppPreferences

@Composable
fun ComponentNavigation(
    parentNav: NavHostController,
    childNav: NavHostController,
    prefs: AppPreferences,
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
        composable(MainRoute.Chart.path) {
            ChartPage()
        }
        composable(MainRoute.Checkbox.path) {
            CheckboxPage()
        }
        composable(MainRoute.Collapsible.path) {
            CollapsiblePage()
        }
        composable(MainRoute.ComboBox.path) {
            ComboBoxPage()
        }
        composable(MainRoute.DataTable.path) {
            DataTablePage()
        }
        composable(MainRoute.DatePicker.path) {
            DatePickerPage()
        }
        composable(MainRoute.DropDown.path) {
            DropDownPage()
        }
        composable(MainRoute.Empty.path) {
            EmptyPage()
        }
        composable(MainRoute.Input.path) {
            InputPage()
        }
        composable(MainRoute.InputOTP.path) {
            InputOTPPage()
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
        composable(MainRoute.Spinner.path) {
            SpinnerPage()
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
