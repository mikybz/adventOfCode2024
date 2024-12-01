import kotlin.math.absoluteValue


fun main(args: Array<String>) = dayRunner(Day01())

class Day01 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val numbers = parse(input)
        val leftList = numbers.map { it.first }.sorted()
        val rightList = numbers.map { it.second }.sorted()
        val zipped = leftList.zip(rightList)
        return zipped.map { it.first - it.second }.sumOf { it.absoluteValue }
    }

    override fun part2(input: List<String>): Any {
        val numbers = parse(input)
        val leftList = numbers.map { it.first }
        val rightList = numbers.map { it.second }
        return leftList.sumOf { rightList.count { inner -> inner == it } * it }
    }

    private fun parse(input: List<String>): List<Pair<Int, Int>> {
        return input.map {
            val splits = it.split("  ").map { it.trim() }
            val (a, b) = splits.map { it.toInt() }
            Pair(a, b)
        }
    }
}