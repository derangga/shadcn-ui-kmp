package dr.shadcn.kmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.rememberNavController
import com.shadcn.ui.components.sooner.ObserveAsEvent
import com.shadcn.ui.components.sooner.SonnerHost
import com.shadcn.ui.components.sooner.SonnerProvider
import com.shadcn.ui.components.sooner.showSonner
import com.shadcn.ui.themes.ShadcnTheme
import dr.shadcn.kmp.navigation.AppNavigation
import dr.shadcn.kmp.themes.ThemeEvent
import dr.shadcn.kmp.themes.ThemeObserver
import dr.shadcn.kmp.themes.ThemeProvider
import dr.shadcn.kmp.themes.getStyles
import dr.shadcn.kmp.themes.isDarkTheme
import kotlinx.coroutines.launch

@Composable
fun App(
    preferences: DataStore<Preferences>
) {
    val systemTheme = isSystemInDarkTheme()
    val localStyles by preferences.getStyles().collectAsState(initial = "Default")

    var isDarkMode by remember { mutableStateOf(false) }
    var styles by remember { mutableStateOf(getStyles()) }

    LaunchedEffect(localStyles) {
        styles = getStyles(localStyles)
    }

    ShadcnTheme(
        isDarkTheme = isDarkMode,
        shadcnLightColors = styles.first,
        shadcnDarkColors = styles.second
    ) {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()
        ObserveAsEvent(SonnerProvider.events) { event ->
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSonner(event)

                if (result == SnackbarResult.ActionPerformed) {
                    event.action?.execute()
                }
            }
        }
        ThemeObserver(ThemeProvider.events) { event ->
            when (event) {
                is ThemeEvent.Styles -> {
                    styles = event.styles
                }
                is ThemeEvent.Theme -> {
                    isDarkMode = isDarkTheme(event.theme, systemTheme)
                }
            }
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SonnerHost(hostState = snackbarHostState)
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { ip ->
            AppNavigation(
                navController = navController,
                prefs = preferences,
                modifier = Modifier.padding(ip),
                isDark = isDarkMode
            )
        }
    }
}