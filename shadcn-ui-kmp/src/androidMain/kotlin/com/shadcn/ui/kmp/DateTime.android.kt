package com.shadcn.ui.kmp

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(
    format: String
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())

actual fun LocalDate.format(
    format: String
): String = DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDate())