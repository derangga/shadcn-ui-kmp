# Shadcn-Compose

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Shadcn-Compose** is a UI kit for Android Jetpack Compose, inspired by the principles and components of [shadcn/ui](https://ui.shadcn.com/). This library provides a set of beautifully designed and customizable Compose components to help you build modern and consistent user interfaces with ease.

## Overview

This project aims to bring the elegance and developer experience of shadcn/ui to the world of Android Jetpack Compose. We follow similar design philosophies, focusing on:

*   **Accessibility:** Components are designed with accessibility in mind.
*   **Customization:** Easily themeable and adaptable to your application's specific needs.
*   **Developer Experience:** Simple and intuitive APIs for a smooth development process.

For more information on the original shadcn/ui, please visit the [official shadcn/ui documentation](https://ui.shadcn.com/docs).

## Requirements

- Kotlin 2.2.0
- Jetpack compose bom 2025.07.00
- Target minSDK 26: Required due to the use of `java.time` APIs (e.g., `DayOfWeek.getDisplayName`) in the Calendar component, which are available from API level 26.

## Installation

// TODO: Add installation instructions

## Usage

Update your application theme to use Shadcn-Compose's theme:
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShadcnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                    ) {
                        var nameTxt by remember { mutableStateOf("John Doe") }
                        var userNameTxt by remember { mutableStateOf("johndoe") }
                        Text(
                            text = "Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.shadcnColors.foreground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Make changes to your account here. Click save when you're done.",
                            fontSize = 14.sp,
                            color = MaterialTheme.shadcnColors.mutedForeground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Name",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.shadcnColors.foreground
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
                            color = MaterialTheme.shadcnColors.foreground
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
    }
}
```

## Components Availability

Here's a list of currently available components:

*   **Accordion**
*   **Alert**
*   **AlertDialog**
*   **Avatar**
*   **Badge**
*   **Button**
*   **Calendar**
*   **Card**
*   **Checkbox**
*   **Combobox**
*   **DatePicker**
*   **Dialog**
*   **DropdownMenu**
*   **Input** (TextField)
*   **Popover**
*   **Progress**
*   **RadioGroup**
*   **Select**
*   **Bottom Sheet**
*   **Sidebar**
*   **Skeleton**
*   **Slider**
*   **Switch**
*   **Sonner**
*   **Tabs**

