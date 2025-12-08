package com.shadcn.ui.components.sooner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * A Composable function that observes a [Flow] and treats each emission as a one-time event.
 *
 * This is useful for scenarios where you want to react to events from a ViewModel or another
 * part of your application without recomposing the entire Composable. The event is consumed
 * only when the Composable is in the `STARTED` lifecycle state.
 *
 * @param T The type of the event emitted by the flow.
 * @param flow The [Flow] of events to observe.
 * @param key1 An optional key to restart the effect if its value changes.
 * @param key2 Another optional key to restart the effect if its value changes.
 * @param onEvent A lambda that will be invoked for each event emitted by the [flow].
 */
@Composable
fun <T> ObserveAsEvent(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}