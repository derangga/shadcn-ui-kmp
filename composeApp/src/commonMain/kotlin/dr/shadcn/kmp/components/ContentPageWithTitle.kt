package dr.shadcn.kmp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

@Composable
fun ContentPageWithTitle(
    title: String? = null,
    modifier: Modifier = Modifier,
    buttonComponent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        title?.let {
            Text(it)
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .border(
                    1.dp,
                    MaterialTheme.styles.border,
                    RoundedCornerShape(MaterialTheme.radius.lg)
                )
                .padding(vertical = 24.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            buttonComponent()
        }
    }
}