package dr.shadcn.kmp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App(remember { createDataStore() })
}