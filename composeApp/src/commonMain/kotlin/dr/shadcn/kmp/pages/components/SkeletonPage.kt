package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Skeleton
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun SkeletonPage() {
    Layout {
        Text(
            "Skeleton",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Use to show a placeholder while content is loading.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle(
            "1. Simple skeleton with different high",
            modifier = Modifier.background(
                MaterialTheme.styles.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Skeleton(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                )
                Skeleton(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(16.dp)
                )
                Skeleton(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                )
            }
        }

        ContentPageWithTitle(
            "2. Simple avatar with title and subtitle skeleton",
            modifier = Modifier.background(
                MaterialTheme.styles.background
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Skeleton(
                    modifier = Modifier
                        .size(64.dp),
                    shape = CircleShape
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )

                    // Example 2: Longer text line skeleton
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                }
            }
        }
    }
}