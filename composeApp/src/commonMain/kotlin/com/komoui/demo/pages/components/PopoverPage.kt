package com.komoui.demo.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.komoui.components.Button
import com.komoui.components.ButtonSize
import com.komoui.components.ButtonVariant
import com.komoui.components.Popover
import com.komoui.demo.components.Layout

@Composable
fun PopoverPage() {
    Layout {
        var showPopover by remember { mutableStateOf(false) }

        Text(
            "Popover",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Popover(
                open = showPopover,
                onDismissRequest = { showPopover = false },
                trigger = {
                    Button(onClick = { showPopover = !showPopover }) {
                        Text("Open Popover")
                    }
                },
            ) {
                Column(
                    modifier = Modifier.padding(8.dp), // Inner padding for content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Place any content here.")
                    Text("This is your popover content.")
                    Button(
                        onClick = { showPopover = false },
                        variant = ButtonVariant.Secondary,
                        size = ButtonSize.Sm
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}