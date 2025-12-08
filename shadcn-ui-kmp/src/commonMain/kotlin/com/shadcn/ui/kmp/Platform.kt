package com.shadcn.ui.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

expect fun platform(): String

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp