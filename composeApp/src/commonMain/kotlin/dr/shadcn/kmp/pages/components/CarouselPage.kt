package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PageSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Card
import com.shadcn.ui.components.Carousel
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import org.jetbrains.compose.resources.painterResource
import shadcnkmp.composeapp.generated.resources.Res
import shadcnkmp.composeapp.generated.resources.store_1
import shadcnkmp.composeapp.generated.resources.store_2
import shadcnkmp.composeapp.generated.resources.store_3
import shadcnkmp.composeapp.generated.resources.store_h_1
import shadcnkmp.composeapp.generated.resources.store_h_2
import shadcnkmp.composeapp.generated.resources.store_h_3

@Composable
fun CarouselPage() {
    val verticalAsset = listOf(Res.drawable.store_1, Res.drawable.store_2, Res.drawable.store_3)
    val horizontalAsset = listOf(Res.drawable.store_h_1, Res.drawable.store_h_2, Res.drawable.store_h_3)
    Layout {
        Text(
            "Carousel",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A carousel with motion and swipe",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Example 1: Basic Carousel
        ContentPageWithTitle("1. Basic carousel") {
            Carousel(
                itemCount = verticalAsset.size,
                modifier = Modifier.fillMaxWidth(),
                showIndicator = true
            ) { position ->
                Card {
                    Image(
                        painterResource(verticalAsset[position]),
                        contentDescription = "asset-${position}",
                        modifier = Modifier
                            .height(400.dp)
                    )
                }
            }
        }

        ContentPageWithTitle("2. Carousel with auto scroll") {
            Carousel(
                itemCount = verticalAsset.size,
                autoScroll = true,
                showIndicator = true,
                autoScrollDelayMillis = 2500L, // Shorter interval for demo
                modifier = Modifier.fillMaxWidth()
            ) { position ->
                Card {
                    Image(
                        painterResource(verticalAsset[position]),
                        contentDescription = "asset-${position}",
                        modifier = Modifier.height(400.dp)
                    )
                }
            }
        }

        ContentPageWithTitle("3. Carousel with custom width size") {
            Carousel(
                itemCount = horizontalAsset.size,
                showIndicator = true,
                pageSize = PageSize.Fixed(300.dp),
                modifier = Modifier.fillMaxWidth(),
                itemSpacing = 12.dp
            ) { position ->
                Card {
                    Image(
                        painterResource(horizontalAsset[position]),
                        contentDescription = "asset-${position}",
                        modifier = Modifier.height(200.dp)
                    )
                }
            }
        }
    }
}