interface DayAdvent {
    fun part1(input: List<String>): Any
    fun part2(input: List<String>): Any
}

val globalDebugStates = mutableListOf<Any>()
var globalTracing = true
fun <T> List<T>.trackDebugState(reset: Boolean = false, state: Any? = null): List<T> =
    also {
        if (globalTracing) {
            if (reset) globalDebugStates.clear()
            globalDebugStates.add(state ?: this)
        }
    }