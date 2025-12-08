package dr.shadcn.kmp.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.Calendar
import com.shadcn.ui.components.Card
import com.shadcn.ui.components.CardContent
import com.shadcn.ui.components.Input
import com.shadcn.ui.themes.styles
import com.shadcn.ui.utils.now
import dr.shadcn.kmp.components.Layout
import kotlinx.datetime.LocalDate

@Composable
fun HomePage(parentNav: NavHostController) {
    val styles = MaterialTheme.styles
    Layout {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Build beautiful components faster",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
            Text(
                "shadcn-compose is inspired by shadcn, providing beautifully designed components that you can copy and paste into your apps. Accessible, customizable, and open source.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
//                        parentNav.navigate(TopLevelRoute.WebviewGraph.pathWithSlug(WebViewSlug.Documentation))
                    }
                ) {
                    Text("Visit Documentation")
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 28.dp, bottom = 28.dp),
            color = styles.muted
        )

        Text(
            "Examples",
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LoginSample()
            CalendarSample()
        }
    }
}

@Composable
private fun LoginSample() {
    var nameTxt by remember { mutableStateOf("John Doe") }
    var userNameTxt by remember { mutableStateOf("johndoe") }
    Card {
        CardContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Make changes to your account here. Click save when you're done.",
                    fontSize = 14.sp,
                    color = MaterialTheme.styles.mutedForeground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Name",
                    fontWeight = FontWeight.SemiBold,
                )
                Input(
                    value = nameTxt,
                    onValueChange = { nameTxt = it },
                    placeholder = "Enter your name",
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Username",
                    fontWeight = FontWeight.SemiBold,
                )
                Input(
                    value = userNameTxt,
                    onValueChange = { userNameTxt = it },
                    placeholder = "Enter your username",
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {}) {
                    Text("Save changes")
                }
            }
        }
    }
}

//@Composable
//fun ChartSample() {
//    val groupedChartData = listOf(
//        GroupedChartEntry("Jan", listOf(100f, 120f, 150f)),
//        GroupedChartEntry("Feb", listOf(110f, 130f, 160f)),
//        GroupedChartEntry("Mar", listOf(90f, 110f, 140f)),
//        GroupedChartEntry("Apr", listOf(130f, 150f, 180f)),
//        GroupedChartEntry("May", listOf(80f, 170f, 165f)),
//    )
//    val datasetLabels = listOf("Series A", "Series B", "Series C")
//
//    Card {
//        CardContent {
//
//
//            GroupedBarChart(
//                data = groupedChartData,
//                datasetLabels = datasetLabels,
//                descriptionText = "Quarterly Performance",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//            )
//        }
//    }
//}

@Composable
fun CalendarSample() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    Card {
        CardContent(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Calendar(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                },
            )
        }
    }
}