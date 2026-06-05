package com.komoui.demo

import com.komoui.demo.themes.AppPreferences
import com.komoui.demo.themes.InMemoryAppPreferences

fun createInMemoryPreferences(): AppPreferences = InMemoryAppPreferences()

internal const val DATA_STORE_FILE_NAME = "themes.preferences_pb"
