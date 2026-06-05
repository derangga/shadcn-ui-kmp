package com.komoui.demo

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
import androidx.navigation.compose.rememberNavController
import com.komoui.components.sooner.ObserveAsEvent
import com.komoui.components.sooner.SonnerHost
import com.komoui.components.sooner.SonnerProvider
import com.komoui.components.sooner.showSonner
import com.komoui.themes.KomoTheme
import com.komoui.demo.navigation.AppNavigation
import com.komoui.demo.themes.ThemeEvent
import com.komoui.demo.themes.ThemeObserver
import com.komoui.demo.themes.AppPreferences
import com.komoui.demo.themes.ThemeProvider
import com.komoui.demo.themes.getStyles
import com.komoui.demo.themes.isDarkTheme
import kotlinx.coroutines.launch

@Composable
fun App(
    preferences: AppPreferences
) {
    val systemTheme = isSystemInDarkTheme()
    val localStyles by preferences.styles.collectAsState(initial = "Default")

    var isDarkMode by remember { mutableStateOf(false) }
    var styles by remember { mutableStateOf(getStyles()) }

    LaunchedEffect(localStyles) {
        styles = getStyles(localStyles)
    }

    KomoTheme(
        isDarkTheme = isDarkMode,
        komoLightColors = styles.first,
        komoDarkColors = styles.second
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
