/**
 * Used during debugging, to track and inspect states during a BREAKPOINT
 */
fun <T> List<T>.trackDebugState(state: Any? = null, reset: Boolean = false): List<T> =
    also {
        trackDebugStateValue(state = state ?: this, reset = reset)
    }

fun trackDebugStateValue(state: Any, reset: Boolean = false) {
    if (globalTracing) {
        if (reset) debugStates.clear()
        debugStates.add(state)
    }
}

val debugStates = mutableListOf<Any>()
var globalTracing = true
var globalDebugPrint = true


