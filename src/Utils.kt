import java.io.File
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.system.measureTimeMillis

/**
 * Being executed each day
 * Data files are named after Class file
 **/
fun dayRunner(dayAdvent: DayAdvent) {
    val day = dayAdvent.javaClass.simpleName
    val testData = readInput("${day}_test")
    val data = readInput(day)

//    println("\nStarting: $day")
//    println("$day Tester1 runtime: ${measureTimeMillis{
//        println("$day Tester1 results: ${dayAdvent.part1(testData)}")
//    }}ms")
//
//    println("$day Result1 runtime: ${measureTimeMillis{
//        println("$day Result1 results: ${dayAdvent.part1(data)}")
//    }}ms")
//
//    println("$day Tester2 runtime: ${measureTimeMillis{
//        println("$day Tester2 results: ${dayAdvent.part2(testData)}")
//    }}ms")
//
//    println("$day Result2 runtime: ${measureTimeMillis{
//        println("$day Result2 results: ${dayAdvent.part2(data)}")
//    }}ms")

    println("$day Tester1: ${dayAdvent.part1(testData)}")
    println("$day Result1: ${dayAdvent.part1(data)}")
    println("$day Tester2: ${dayAdvent.part2(testData)}")
    println("$day Result2: ${dayAdvent.part2(data)}")
}

//fun readInput(name: String) = File("adventofcode2022/src/data", "$name.txt").readLines()
fun readInput(name: String) = File("src/data", "$name.txt").readLines()

/**
 * Split a lists into sublist
 * Do the sublist split on list items that matches given predicate
 * Used in Day01 Day05
 **/
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

/**
 * An ArrayDeque is an efficient and safe implementation of a
 * double ended queue, that also serve as a stack
 * Used in Day5
 */
typealias CharStack = ArrayDeque<Char>

fun <T> Collection<T>.toArrayDeque(): ArrayDeque<T> {
    return ArrayDeque(this)
}

// Day07
fun printIndent(level: Int) {
    repeat(level) { print("  ") }
}

// Day08
//fun transpose(trees: List<List<Int>>): Array<IntArray> {
//    val oldXtopIdx = trees.size - 1
//    val oldYtopIdx = trees[0].size - 1
//    val trans = Array(oldXtopIdx, { IntArray(oldYtopIdx)})
//    for (x in 0..oldXtopIdx) {
//        for (y in 0..oldYtopIdx) {
//            trans[x][y]=trees[y][x]
//        }
//    }
//    return trans
//}

fun <T> printMatrix(matrix: Collection<Collection<T>>) {
    println(matrix.joinToString(separator = "\n") { it.joinToString(separator = "") })
}


fun <T> transpose(input: List<List<T>>): List<List<T>> =
    (0..input[0].size - 1)
        .map { y ->
            (0..input.size - 1)
                .map { x -> input[x][y] }
        }

// Day07
fun isLong(input: String): Boolean = input.toLongOrNull() != null

enum class Part {
    Part1, Part2
}

// Day13
/** Regex split that includes the split-pattern in the output list
 * Example will split on "!" OR "?":
 *      input.splitIncluding("[!?]")
 **/
fun String.splitIncluding(split: String): List<String> {
    return this.split(Regex("(?<=${split})|(?=${split})")).filter { it.isNotEmpty() }
}

/** The native range does not support counting backwards, but this do */
infix fun Int.towards(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}


//    val path = dayAdvent.javaClass.protectionDomain.codeSource.location.path!!
//    val projectName = "/adventofcode2022/"
//    val dataPath=path.substringBeforeLast(projectName)+projectName+"src/data"
//    val testData = readInputFromPath(dataPath,"${day}_testx")
//    val data = readInputFromPath(dataPath,("${day}"))
//  fun readInputFromPath(path: String, name: String) = File(path, "$name.txt").readLines()