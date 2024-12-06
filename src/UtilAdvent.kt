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
    println("  $results")
    println("  Execution time1: $executionTime1 ms")
    println("  Execution time2: $executionTime2 ms")
    return results
}

data class AdventResults(
    var day: String, // Add a new field to store the day
    var part1Test: String = "",
    var part1: String = "",
    var part2Test: String = "",
    var part2: String = ""
) {
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
