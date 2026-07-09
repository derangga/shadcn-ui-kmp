package com.komoui.demo.pages.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.komoui.components.Button
import com.komoui.components.Card
import com.komoui.components.CardContent
import com.komoui.components.CardDescription
import com.komoui.components.CardFooter
import com.komoui.components.CardHeader
import com.komoui.components.CardTitle
import com.komoui.demo.MainRoute
import com.komoui.demo.components.ContentPageWithTitle
import com.komoui.demo.components.Layout

@Composable
fun CardPage(nav: NavHostController) {
    Layout {
        Text(
            "Card",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic usage") {
            Card {
                CardHeader {
                    CardTitle { Text("This is title") }
                    CardDescription { Text("This is description") }
                }
                CardContent {
                    Text("This is the main content of the card.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You can put any composables here.")
                }
                CardFooter {
                    Text("This is footer")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        ContentPageWithTitle("2. With background image") {
            Card(
                modifier = Modifier.border(0.dp, Color.Unspecified)
            ) {
                AsyncImage(
                    "https://img.magnific.com/free-photo/young-woman-listening-music-home_23-2149029679.jpg",
                    contentDescription = "sample image",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 24.dp),
            onClick = {
                nav.navigate(MainRoute.CardPlayground.path)
            }
        ) {
            Text("Try card playground")
        }
    }
}
