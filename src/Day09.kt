fun main() = dayRunner(Day09())

class Day09 : DayAdvent {

    override fun part1(input: List<String>): Any {
        return "Not implemented"
    }

    override fun part2(input: List<String>): Any {
        return "Not implemented"
    }

    fun List<String>.parsePairs(): List<Pair<Int, Int>> = map { line ->
        line.split("  ", limit=2)
            .map(String::trim)
            .let { (a, b) -> a.toInt() to b.toInt() }
    }
}