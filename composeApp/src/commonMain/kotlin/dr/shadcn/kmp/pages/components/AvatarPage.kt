package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Avatar
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun AvatarPage() {
    Layout {
        Text(
            "Avatar",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "An image element with a fallback for representing the user.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Avatar with image
        ContentPageWithTitle("1. Avatar with image") {
            Avatar(
                model = "https://avatars.githubusercontent.com/u/124599?v=4", // Example image URL
                contentDescription = "Avatar",
                fallbackText = "CN"
            )
        }

        // Avatar with fallback (initials) - no imageUrl provided
        ContentPageWithTitle("2. Avatar with fallback (initials - no imageUrl provided") {
            Avatar(
                null,
                fallbackText = "CN"
            )
        }

        // Avatar with a larger size and fallback
        ContentPageWithTitle("3. Avatar with larger size and fallback") {
            Avatar(
                null,
                size = 64.dp,
                fallbackText = "JD"
            )
        }

        // Avatar with image that might fail to load, showing custom error content
        ContentPageWithTitle("4. Avatar with image that might fail to load, showing custom error content") {
            Avatar(
                model = "https://invalid-image-url.com/nonexistent.png",
                size = 80.dp,
                contentDescription = "Invalid Avatar",
                fallbackText = "ERR",
                errorContent = {
                    Text("❌", fontSize = 32.sp)
                }
            )
        }

        // Avatar with a valid image and content description, showing custom loading content
        ContentPageWithTitle("5. Avatar with a valid image and content description, showing custom loading content") {
            Avatar(
                model = "https://picsum.photos/200/300", // Another example image URL
                size = 50.dp,
                contentDescription = "Random Image",
                fallbackText = "RI",
                loadingContent = {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.styles.primary)
                }
            )
        }

        // Avatar with a valid image, but no custom loading/error content, so fallbackText is used
        ContentPageWithTitle("6. Avatar with a valid image, but no custom loading/error content, so fallbackText is used") {
            Avatar(
                model = "https://picsum.photos/seed/picsum/200/300",
                size = 50.dp,
                contentDescription = "Another Random Image",
                fallbackText = "AR"
            )
        }
    }
}