package com.shadcn.ui.kmp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual fun platform() = "Android"

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp
