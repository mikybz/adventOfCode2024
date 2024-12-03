fun main(args: Array<String>) = dayRunner(Day02())

class Day02 : DayAdvent {

    override fun part1(input: List<String>): Any = // 516
        input.parse()
            .count { it.isStrictlyMonotonic() }

    override fun part2(input: List<String>): Any = // 561
        input.parse()
            .count { it.isMonotonicDampened() }

    private fun List<String>.parse(): List<List<Int>> =
        map { it.split("\\s+".toRegex()).map(String::toInt) }

    private fun List<Int>.isStrictlyMonotonic(): Boolean =
        isStrictlyIncreasing() || isStrictlyDecreasing()

    private fun List<Int>.isStrictlyIncreasing(): Boolean =
        windowed(2).all { (a, b) -> b - a in 1..3 }

    private fun List<Int>.isStrictlyDecreasing(): Boolean =
        windowed(2).all { (a, b) -> a - b in 1..3 }

    private fun List<Int>.isMonotonicDampened(): Boolean =
        isStrictlyMonotonic() || indices.any { index ->
            filterIndexed { i, _ -> i != index }.isStrictlyMonotonic()
        }
}