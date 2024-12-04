import kotlin.math.absoluteValue


fun main(args: Array<String>) = dayRunner(Day01())

class Day01 : DayAdvent {

    override fun part1(input: List<String>): Any =
        input.parsePairs()
            .let { pairs ->
                pairs.map { it.first }.sorted()
                    .zip(pairs.map { it.second }.sorted())
                    .sumOf { (a, b) -> (a - b).absoluteValue }
            }

    override fun part2(input: List<String>): Any =
        input.parsePairs()
            .let { pairs ->
                val leftList = pairs.map { it.first }
                val rightList = pairs.map { it.second }
                leftList.sumOf { left -> rightList.count { it == left } * left }
            }

    fun List<String>.parsePairs(): List<Pair<Int, Int>> = map { line ->
        line.split("  ", limit=2)
            .map(String::trim)
            .let { (a, b) -> a.toInt() to b.toInt() }
    }
}