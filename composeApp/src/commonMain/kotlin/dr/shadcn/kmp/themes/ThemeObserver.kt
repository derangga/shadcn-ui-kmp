package dr.shadcn.kmp.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.shadcn.ui.themes.DarkStyles
import com.shadcn.ui.themes.LightStyles
import com.shadcn.ui.themes.ShadcnStyles
import dr.shadcn.kmp.themes.styles.AmberMinimal
import dr.shadcn.kmp.themes.styles.AmberMinimalDark
import dr.shadcn.kmp.themes.styles.BoltTech
import dr.shadcn.kmp.themes.styles.BoltTechDark
import dr.shadcn.kmp.themes.styles.Bubblegum
import dr.shadcn.kmp.themes.styles.BubblegumDark
import dr.shadcn.kmp.themes.styles.Caffeine
import dr.shadcn.kmp.themes.styles.CaffeineDark
import dr.shadcn.kmp.themes.styles.Catppuccin
import dr.shadcn.kmp.themes.styles.CatppuccinDark
import dr.shadcn.kmp.themes.styles.Claude
import dr.shadcn.kmp.themes.styles.ClaudeDark
import dr.shadcn.kmp.themes.styles.ModernMinimal
import dr.shadcn.kmp.themes.styles.ModernMinimalDark
import dr.shadcn.kmp.themes.styles.NeoBrutalism
import dr.shadcn.kmp.themes.styles.NeoBrutalismDark
import dr.shadcn.kmp.themes.styles.OceanBreeze
import dr.shadcn.kmp.themes.styles.OceanBreezeDark
import dr.shadcn.kmp.themes.styles.PastelDreams
import dr.shadcn.kmp.themes.styles.PastelDreamsDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

object StylesSelection {
    val styles = listOf(
        "Default",
        "Amber Minimal",
        "Bolt Tech",
        "Bubblegum",
        "Catppuccin",
        "Caffeine",
        "Claude",
        "Modern Minimal",
        "NeoBrutalism",
        "Ocean Breeze",
        "Pastel Dreams",
    )
}

@Composable
fun <T> ThemeObserver(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

object ThemeProvider {
    private val _events = Channel<ThemeEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(themes: ThemeEvent) {
        _events.send(themes)
    }
}

internal fun isDarkTheme(theme: String, systemTheme: Boolean): Boolean {
    return when (theme) {
        "light" -> false
        "dark" -> true
        else -> systemTheme
    }
}

internal fun getStyles(styles: String = "Default"): Pair<ShadcnStyles, ShadcnStyles> {
    return when (styles) {
        "Bubblegum" -> Bubblegum to BubblegumDark
        "Amber Minimal" -> AmberMinimal to AmberMinimalDark
        "Bolt Tech" -> BoltTech to BoltTechDark
        "Catppuccin" -> Catppuccin to CatppuccinDark
        "Caffeine" -> Caffeine to CaffeineDark
        "Claude" -> Claude to ClaudeDark
        "Modern Minimal" -> ModernMinimal to ModernMinimalDark
        "NeoBrutalism" -> NeoBrutalism to NeoBrutalismDark
        "Ocean Breeze" -> OceanBreeze to OceanBreezeDark
        "Pastel Dreams" -> PastelDreams to PastelDreamsDark
        else -> LightStyles to DarkStyles
    }
}