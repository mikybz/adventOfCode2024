import java.io.File
import kotlin.system.measureTimeMillis

interface DayAdvent {
    fun part1(input: List<String>): Any?
    fun part2(input: List<String>): Any?
}

fun dayRunner(dayAdvent: DayAdvent) {
    dayRunWithResult(dayAdvent)
}

fun dayRunWithResult(dayAdvent: DayAdvent): AdventResults {
    val day = dayAdvent.javaClass.simpleName
    val testData = readInput("${day}_test")
    val data = readInput(day)

    val part1Test = dayAdvent.part1(testData).toString()
    var part1: String
    val executionTime1 = measureTimeMillis {
        part1 = dayAdvent.part1(data).toString()
    }

    val part2Test = dayAdvent.part2(testData).toString()
    var part2: String
    val executionTime2 = measureTimeMillis {
        part2 = dayAdvent.part2(data).toString()
    }

    val results = AdventResults(
        day = day,
        part1Test = part1Test,
        part1 = part1,
        part2Test = part2Test,
        part2 = part2
    )
    //println("  $results")
    println("  $day")
    println("    Results: ${results.toConsole()}")
    println("    Runtime: part1=${executionTime1}ms  part2=${executionTime2}ms")
    return results
}

data class AdventResults(
    var day: String, // Add a new field to store the day
    var part1Test: String = "",
    var part1: String = "",
    var part2Test: String = "",
    var part2: String = ""
) {
    fun toConsole(): String {
        return "part1Test=$part1Test, part1=$part1, part2Test=$part2Test, part2=$part2"
    }
}

fun readAdventResults(): List<AdventResults> {
    val results = mutableListOf<AdventResults>()
    val regex = Regex("""AdventResults\(day=(.*?), part1Test=(.*?), part1=(.*?), part2Test=(.*?), part2=(.*?)\)""")

    readInput("Solutions").forEach { line ->
        val matchResult = regex.matchEntire(line)
        if (matchResult != null) {
            val (day, part1Test, part1, part2Test, part2) = matchResult.destructured
            results.add(AdventResults(day, part1Test, part1, part2Test, part2))
        } else {
            if (line.isNotEmpty()) throw IllegalArgumentException("Invalid format: $line")
        }
    }
    return results
}

fun readInput(name: String) = File("src/data", "$name.txt").readLines()



data class Pos(val y: Int, val x: Int) {
    operator fun plus(direction: Direction): Pos {
        return Pos(y + direction.dy, x + direction.dx)
    }

    operator fun plus(other: Pos): Pos {
        return Pos(y + other.y, x + other.x)
    }

    operator fun times(other: Pos): Pos {
        return Pos(y * other.y, x * other.x)
    }

    operator fun times(other: Int): Pos {
        return Pos(y * other, x * other)
    }

    fun isOutside(matrix: Array<Array<Int>>): Boolean {
        return y < 0 || y >= matrix.size || x < 0 || x >= matrix[y].size
    }
}

//fun List<Pos>.unique(): List<Pos> = this.distinct()

enum class Directions(val pos: Pos) {
    NORTH(Pos(y = -1, x = 0)),
    EAST(Pos(y = 0, x = 1)),
    SOUTH(Pos(y = 1, x = 0)),
    WEST(Pos(y = 0, x = -1)),
    NORTH_EAST(Pos(y = -1, x = 1)),
    SOUTH_EAST(Pos(y = 1, x = 1)),
    SOUTH_WEST(Pos(y = 1, x = -1)),
    NORTH_WEST(Pos(y = -1, x = -1))
}



/** Normal Matrix operations */
fun <T> Array<Array<T>>.getVal(pos: Pos): T = this[pos.y][pos.x]

fun <T> Array<Array<T>>.setVal(pos: Pos, value: T) {
    this[pos.y][pos.x] = value
}

inline fun <reified T> List<List<T>>.toArrayMatrix(): Array<Array<T>> {
    return Array(this.size) { rowIndex ->
        this[rowIndex].toTypedArray()
    }
}
inline fun <reified R> List<String>.toArrayMatrix(transform: (Char) -> R): Array<Array<R>> {
    return this.map { line ->
        line.map { char -> transform(char) }.toTypedArray()
    }.toTypedArray()
}


typealias FastMatrix = IntArray
/** Fast Matrix is implemented as an Integer 1D array, for extremely fast access and initialization */
fun createFastMatrix(dimensions: Pos): IntArray = IntArray(dimensions.y * dimensions.x) { 0 }

fun FastMatrix.setPosFast(pos: Pos, dim: Pos, value: Int) {
    this[dim.y * pos.y + pos.x] = value
}

fun FastMatrix.getPosFast(pos: Pos, dim: Pos) = this[dim.y * pos.y + pos.x]

fun fastMatrixToNormal(matrix: FastMatrix, dimensions: Pos): List<List<Int>> = matrix.toList().chunked(dimensions.x)




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

fun <T> printMatrix(matrix: Array<Array<T>>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}


// Create one list out of two by interleaving elements
fun <T> mergeAlternating(listA: List<T>, listB: List<T>): List<T> {
    val minSize = minOf(listA.size, listB.size)
    val interleaved = (0 until minSize).flatMap { listOf(listA[it], listB[it]) }
    return interleaved + listA.drop(minSize) + listB.drop(minSize)
}


fun <T> transpose(input: List<List<T>>): List<List<T>> =
    (0..input[0].size - 1)
        .map { y ->
            (0..input.size - 1)
                .map { x -> input[x][y] }
        }

