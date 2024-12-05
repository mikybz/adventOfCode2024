import java.io.File

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

    val results = AdventResults(
        day = day,
        part1Test = dayAdvent.part1(testData).toString(),
        part1 = dayAdvent.part1(data).toString(),
        part2Test = dayAdvent.part2(testData).toString(),
        part2 = dayAdvent.part2(data).toString()
    )
    println("  $results")
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

