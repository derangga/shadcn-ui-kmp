package com.komoui.demo.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.komoui.components.DataTable
import com.komoui.components.DataTableColumn
import com.komoui.components.sonner.SonnerProvider
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.demo.components.ContentPageWithTitle
import com.komoui.demo.components.Layout
import kotlinx.coroutines.launch

private data class Payment(
    val id: String,
    val amount: Double,
    val status: String,
    val email: String
)

private val samplePayments = listOf(
    Payment("m5gr84i9", 316.0, "success", "ken99@example.com"),
    Payment("3u1reuv4", 242.0, "success", "Abe45@example.com"),
    Payment("derv1ws0", 837.0, "processing", "Monserrat44@example.com"),
    Payment("5kma53ae", 874.0, "success", "Silas22@example.com"),
    Payment("bhqecj4p", 721.0, "failed", "carmella@example.com"),
    Payment("p83kdj19", 158.0, "pending", "alex@example.com"),
    Payment("qw83lz2d", 492.0, "processing", "kyle@example.com"),
    Payment("vc02nm44", 1023.0, "success", "taylor@example.com"),
    Payment("hp29xq77", 64.5, "pending", "morgan@example.com"),
    Payment("ze45rt81", 389.9, "failed", "jamie@example.com"),
    Payment("yt13pu62", 1450.0, "success", "robin@example.com"),
    Payment("lk88nm05", 215.75, "processing", "casey@example.com"),
    Payment("op56qw18", 78.25, "success", "drew@example.com"),
    Payment("rs90fj37", 612.4, "failed", "sam@example.com"),
    Payment("uv24gh59", 933.6, "success", "quinn@example.com")
)

private fun formatUsd(amount: Double): String {
    val whole = amount.toLong()
    val cents = ((amount - whole) * 100).toLong()
    val centsStr = if (cents < 10) "0$cents" else cents.toString()
    return "$$whole.$centsStr"
}

@Composable
fun DataTablePage() {
    Layout {
        Text(
            "Data Table",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = "A responsive data table with sorting, row selection, and pagination. " +
                    "On mobile, the table scrolls horizontally to fit all columns.",
            style = MaterialTheme.typography.titleMedium
        )

        ContentPageWithTitle {
            PaymentDataTableDemo()
        }
    }
}

@Composable
private fun PaymentDataTableDemo() {
    val scope = rememberCoroutineScope()
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius

    val columns = listOf(
        DataTableColumn<Payment>(
            id = "status",
            header = "Status",
            width = 140.dp,
            cell = { payment ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.sm))
                        .background(styles.muted)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = payment.status.replaceFirstChar { it.uppercase() },
                        color = styles.foreground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        ),
        DataTableColumn<Payment>(
            id = "email",
            header = "Email",
            width = 240.dp,
            sortable = true,
            comparator = compareBy { it.email.lowercase() },
            cell = { payment ->
                Text(
                    text = payment.email,
                    color = styles.foreground,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        ),
        DataTableColumn<Payment>(
            id = "amount",
            header = "Amount",
            width = 120.dp,
            sortable = true,
            comparator = compareBy { it.amount },
            headerContent = {
                Text(
                    text = "Amount",
                    color = styles.mutedForeground,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            cell = { payment ->
                Text(
                    text = formatUsd(payment.amount),
                    color = styles.foreground,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    )

    DataTable(
        items = samplePayments,
        columns = columns,
        rowKey = { it.id },
        modifier = Modifier.fillMaxWidth(),
        enableSelection = true,
        pageSize = 5,
        onSelectionChange = { picked ->
            val subText = if (picked.size > 1) "items" else "item"
            scope.launch {
                SonnerProvider.showMessage(
                    "Picking ${picked.size} $subText",
                    withDismissAction = true
                )
            }
        }
    )
}
