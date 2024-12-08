/**
 * Used during debugging, to track and inspect states during a BREAKPOINT
 */
fun <T> List<T>.trackDebugState(state: Any? = null, reset: Boolean = false): List<T> =
    also {
        trackDebugStateValue(state = state ?: this, reset = reset)
    }

fun trackDebugStateValue(state: Any, reset: Boolean = false) {
    if (globalTracing) {
        if (reset) globalDebugStates.clear()
        globalDebugStates.add(state)
    }
}

val globalDebugStates = mutableListOf<Any>()
var globalTracing = true


