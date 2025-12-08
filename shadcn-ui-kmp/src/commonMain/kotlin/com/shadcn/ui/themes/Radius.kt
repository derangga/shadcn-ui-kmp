package com.shadcn.ui.themes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

interface ShadcnRadius {
    val radius: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xxl: Dp
    val xl3: Dp
    val full: Dp
}

object Radius : ShadcnRadius {
    override val radius: Dp = 8.dp
    override val sm: Dp = max(0.dp, radius - 4.dp)
    override val md: Dp = max(0.dp, radius - 2.dp)
    override val lg: Dp = radius
    override val xl: Dp = max(0.dp, radius + 4.dp)
    override val xxl: Dp = max(0.dp, radius + 8.dp)
    override val xl3: Dp = max(0.dp, radius + 12.dp)
    override val full: Dp = 999.dp
}
