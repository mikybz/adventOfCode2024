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



/**
 * Potential useful helper functions, I have created for previous puzzles
 */

//Split into the sublist based on LINES that matches given predicate
fun <T> List<T>.subListSplitter(predicate: (T) -> Boolean): List<List<T>> =
    flatMapIndexed { index, x ->
        when {
            index == 0 || index == lastIndex -> listOf(index)
            predicate(x) -> listOf(index - 1, index + 1)
            else -> emptyList()
        }
    }.windowed(size = 2, step = 2) { (from, to) ->
        when {
            from <= to -> slice(from..to)
            else -> emptyList()
        }
    }

fun <T> List<T>.swapElements( index1: Int, index2: Int): List<T> {
    // Create a mutable copy of the list
    val newList = toMutableList()

    // Swap the elements
    val temp = newList[index1]
    newList[index1] = newList[index2]
    newList[index2] = temp

    // Return the modified list
    return newList
}

fun <T> printMatrix(matrix: Collection<Collection<T>>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}


fun <T> transpose(input: List<List<T>>): List<List<T>> =
    (0..input[0].size - 1)
        .map { y ->
            (0..input.size - 1)
                .map { x -> input[x][y] }
        }
