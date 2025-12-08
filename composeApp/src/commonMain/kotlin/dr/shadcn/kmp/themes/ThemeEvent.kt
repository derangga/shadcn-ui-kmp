package dr.shadcn.kmp.themes

import com.shadcn.ui.themes.ShadcnStyles

sealed class ThemeEvent {
    data class Styles(val key: String, val styles: Pair<ShadcnStyles, ShadcnStyles>): ThemeEvent()
    data class Theme(val theme: String): ThemeEvent()
}