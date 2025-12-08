package dr.shadcn.kmp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Layout(space: InnerSpace = LayoutDefaults.space(), content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = space.start, end = space.end)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(space.top))
        Column(content = content, verticalArrangement = Arrangement.spacedBy(12.dp))
        Spacer(modifier = Modifier.height(space.bottom))
    }
}

data class InnerSpace(
    val start: Dp,
    val end: Dp,
    val top: Dp,
    val bottom: Dp
)

object LayoutDefaults {
    fun space(start: Dp = 16.dp, end: Dp = 16.dp, top: Dp = 8.dp, bottom: Dp = 24.dp): InnerSpace {
        return InnerSpace(
            start = start,
            end = end,
            top = top,
            bottom = bottom
        )
    }

    fun space(vertical: Dp = 12.dp, horizontal: Dp = 16.dp): InnerSpace {
        return InnerSpace(
            start = horizontal,
            end = horizontal,
            top = vertical,
            bottom = vertical
        )
    }
}