import kotlin.math.absoluteValue

fun main(args: Array<String>) = dayRunner(DayExample())

class DayExample : DayAdvent {

    override fun part1(input: List<String>): Any {
        val numbers = parse(input)
        return "Not implemented"
    }

    override fun part2(input: List<String>): Any {
        val numbers = parse(input)
        return "Not implemented"
    }

    private fun parse(input: List<String>): List<Pair<Int, Int>> {
        return input.map {
            val splits = it.split("  ").map { it.trim() }
            val (a, b) = splits.map { it.toInt() }
            Pair(a, b)
        }
    }
}