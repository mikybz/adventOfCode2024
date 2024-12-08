import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() = dayRunner(Day07())

//val globalMultiThreadEnabled = true

class Day07 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val equations = input.parse()
        val operatorLookups = generateOperatorLookups(equations, 2)
        val sum =
            equations.map { (expected, values) ->
                getResultIfValid1(expected, values, operatorLookups)
            }.sumOf { it ?: 0 }
        return sum
    }

    override fun part2(input: List<String>): Any {
        val equations = input.parse()
        val operatorLookups = generateOperatorLookups(equations, 3)
        val sum =
            if (globalMultiThreadEnabled) {
                processInParallel(equations) { (expected, values) ->
                    getResultIfValid2(expected, values, operatorLookups)
                }.sumOf { it }
            } else {
                equations.map { (expected, values) ->
                    getResultIfValid2(expected, values, operatorLookups)
                }
                    .sumOf { it ?: 0 }
            }
        return sum
    }
}

fun List<String>.parse(): List<Pair<Long, List<Int>>> =
    map { it.split(": ", limit = 2) }
        .map { (a, b) ->
            a.toString().toLong() to b.toString().split(" ").map { it.toInt() }
        }

// Generate all possible operator permutations for the given input equations,
// for each number of operators in the equation
private fun generateOperatorLookups(pairs: List<Pair<Long, List<Int>>>, operatorTypes: Int): Array<Array<Array<Int>>> {
    // Find the maximum number of operators in all the input equations
    val maxOperators = pairs
        .maxOfOrNull { it.second.size - 1 } ?: 0
    return generateOperatorLookups(maxOperators, operatorTypes)
}

// First dimension is the number of operators in the equation
// Second dimension is the number of possible operator permutations
// Third dimension is the operator permutation
// 0 is +, 1 is *, 2 is concatenation
// Arrays are faster than lists for lookups
private fun generateOperatorLookups(maxOperators: Int, operatorNrs: Int): Array<Array<Array<Int>>> {
    var lookupOperatorPermutations: Array<Array<Array<Int>>> = Array(maxOperators) { arrayOf() }
    if (maxOperators < 1) return lookupOperatorPermutations
    val operators: Array<Int> = when (operatorNrs) {
        3 -> arrayOf(0, 1, 2)
        else -> arrayOf(0, 1)
    }

    var operatorBuildIterator = operators.map { arrayOf(it) }.toTypedArray()
    lookupOperatorPermutations[0] = operatorBuildIterator
    for (i in 1 until maxOperators) {
        operatorBuildIterator = operatorBuildIterator.flatMap { op ->
            operators.map { op + it }
        }.toTypedArray()
        lookupOperatorPermutations[i] = operatorBuildIterator
    }
    return lookupOperatorPermutations
}

private fun getResultIfValid1(
    expected: Long,
    testValues: List<Int>,
    operatorLookups: Array<Array<Array<Int>>>
): Long? {
    val allOperators = operatorLookups[testValues.size - 2]

    allOperators.map { operators ->
        val firstValue = testValues.first().toLong()
        val calculated = testValues.drop(1).zip(operators).fold(firstValue) { acc, (value, operator) ->
            if (acc > expected) return@fold acc
            when (operator) {
                0 -> acc + value
                1 -> acc * value
                2 -> acc * Math.pow(10.0, value.toString().length.toDouble()).toLong() + value
                else -> throw IllegalArgumentException("Unknown operator $operator")
            }
        }
        if (calculated == expected) {
            return calculated
        }
    }
    return null
}

// This method is 100% equal to getResultIfValid1, but they are used with different operatorLookups
// Due to how the runtime optimizer works, the code runs much faster like this
private fun getResultIfValid2(
    expected: Long,
    testValues: List<Int>,
    operatorLookups: Array<Array<Array<Int>>>
): Long? {
    val allOperators = operatorLookups[testValues.size - 2]

    allOperators.map { operators ->
        val firstValue = testValues.first().toLong()
        val calculated = testValues.drop(1).zip(operators).fold(firstValue) { acc, (value, operator) ->
            if (acc > expected) return@fold acc
            when (operator) {
                0 -> acc + value
                1 -> acc * value
                2 -> acc * Math.pow(10.0, value.toString().length.toDouble()).toLong() + value
                else -> throw IllegalArgumentException("Unknown operator $operator")
            }
        }
        if (calculated == expected) {
            return calculated
        }
    }
    return null
}

// Used only if globalMultiThreadEnabled is true 
fun <T, R> processInParallel(
    items: List<T>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    task: suspend (T) -> R?
): List<R> = runBlocking {
    items.map { item: T ->
        async(dispatcher) { task(item) }
    }.mapNotNull { it.await() }
}
