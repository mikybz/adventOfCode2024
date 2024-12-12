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


fun MutableMap<Char, MutableList<Pyx>>.insertInto(ch: Char, pyx: Pyx) {
    if (!containsKey(ch)) put(ch, mutableListOf())
    get(ch)!!.add(pyx)
}


data class Pyx(val y: Int, val x: Int) {
    operator fun plus(direction: Direction): Pyx = Pyx(y + direction.dy, x + direction.dx)
    operator fun plus(other: Pyx): Pyx = Pyx(y + other.y, x + other.x)
    operator fun times(other: Pyx): Pyx = Pyx(y * other.y, x * other.x)
    operator fun times(other: Int): Pyx = Pyx(y * other, x * other)
    fun isOutside(matrix: Array<Array<Int>>): Boolean = y < 0 || y >= matrix.size || x < 0 || x >= matrix[y].size
    fun isOutside(matrix: Array<Array<Char>>): Boolean = y < 0 || y >= matrix.size || x < 0 || x >= matrix[y].size
    fun isOutside(dim:Pyx): Boolean = y<0 || y>=dim.y ||  x < 0 || x >= dim.x
    fun isOutside(matrix: Array<CharArray>): Boolean = y < 0 || y >= matrix.size || x < 0 || x >= matrix[y].size
    fun get4Neighbours(): List<Pyx> = listOf<Pyx>(Pyx(y - 1, x), Pyx(y + 1, x), Pyx(y, x - 1), Pyx(y, x + 1))
    override fun toString(): String = "($y, $x)"
    fun toList(): List<Int> = listOf(x, y)
    companion object {
        fun ofXY(x: Int, y: Int) = Pyx(y, x)
        fun ofYX(y: Int, x: Int) = Pyx(y, x)
    }
}

enum class Directions(val pyx: Pyx) {
    NORTH(Pyx(y = -1, x = 0)),
    EAST(Pyx(y = 0, x = 1)),
    SOUTH(Pyx(y = 1, x = 0)),
    WEST(Pyx(y = 0, x = -1)),
    NORTH_EAST(Pyx(y = -1, x = 1)),
    SOUTH_EAST(Pyx(y = 1, x = 1)),
    SOUTH_WEST(Pyx(y = 1, x = -1)),
    NORTH_WEST(Pyx(y = -1, x = -1))
}

/** Normal Matrix operations */
inline fun <T> Array<Array<T>>.getVal(pyx: Pyx): T = this[pyx.y][pyx.x]

@Suppress("unused")
fun <T> Array<Array<T>>.setVal(pyx: Pyx, value: T) {
    this[pyx.y][pyx.x] = value
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

fun List<String>.toCharArrayMatrix(): Array<CharArray> {
    return this.map { line ->
        line.toCharArray()
    }.toTypedArray()
}

typealias FastMatrix = IntArray

/** Fast Matrix is implemented as an Integer 1D array, for extremely fast access and initialization */
fun createFastMatrix(dimensions: Pyx): IntArray = IntArray(dimensions.y * dimensions.x) { 0 }

fun FastMatrix.setPosFast(pyx: Pyx, dim: Pyx, value: Int) {
    this[dim.y * pyx.y + pyx.x] = value
}

fun FastMatrix.getPosFast(pyx: Pyx, dim: Pyx) = this[dim.y * pyx.y + pyx.x]

@Suppress("unused")
fun fastMatrixToNormal(matrix: FastMatrix, dimensions: Pyx): List<List<Int>> = matrix.toList().chunked(dimensions.x)


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

fun <T> List<T>.swapElements(index1: Int, index2: Int): List<T> {
    // Create a mutable copy of the list
    val newList = toMutableList()

    // Swap the elements
    val temp = newList[index1]
    newList[index1] = newList[index2]
    newList[index2] = temp

    // Return the modified list
    return newList
}

@Suppress("unused")
fun <T> printMatrix(matrix: Collection<Collection<T>>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}

fun <T> printMatrix(matrix: Array<Array<T>>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}

fun printMatrix(matrix: Array<CharArray>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}


fun printOverlappedMatrix(
    matrix: Array<Array<Char>>,
    overlappedPositions: List<Pyx>,
    emptyChar: Char = '.'
) {
    // Print header numbers
    matrix.indices.map { if (it > 10 && it % 10 != 0) it % 10 else it }
        .joinToString(separator = "", prefix = "   ") {
            it.toString().padEnd(2, padChar = ' ')
        }.let { println(it) }

    // Print matrix, starting with row numbers, overlapped positions are marked with #
    matrix.forEachIndexed { y, line ->
        print(y.toString().padEnd(3, padChar = ' '))
        line.forEachIndexed { x, letter ->
            //if (letter == '.') return@forEachIndexed // Next x
            if (overlappedPositions.contains(Pyx(y, x))) {
                when (letter) {
                    emptyChar -> print("# ")
                    else ->
                        print("${letter}#")
                }
            } else {
                print("${letter} ")
            }
        }
        println()
    }
}

// Create one list out of two by interleaving elements
@Suppress("unused")
fun <T> mergeAlternating(listA: List<T>, listB: List<T>): List<T> {
    val minSize = minOf(listA.size, listB.size)
    val interleaved = (0 until minSize).flatMap { listOf(listA[it], listB[it]) }
    return interleaved + listA.drop(minSize) + listB.drop(minSize)
}

@Suppress("unused")
fun <T> transpose(input: List<List<T>>): List<List<T>> =
    (0..input[0].size - 1)
        .map { y ->
            (0..input.size - 1)
                .map { x -> input[x][y] }
        }

