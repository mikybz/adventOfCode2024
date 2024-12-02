fun main(args: Array<String>) = dayRunner(Day02())

class Day02 : DayAdvent {

    override fun part1(input: List<String>): Any =
        input.parse()
            .map { it.safeIncrease() || it.safeDecrease() }
            .count { it == true }

    override fun part2(input: List<String>): Any {
        val numbers = input.parse()
        val safe = numbers.map { it.safeIncreaseDampened() || it.safeDecreaseDampened() }
        val sum = safe.count { it == true }
        return sum
    }

    // 554 is to low
    // 561 correct

    private fun List<String>.parse(): List<List<Int>> =
        map { line ->
            line.run {
                split("\\s+".toRegex())
                    .map { it.toInt() }
            }
        }

    private fun List<Int>.safeIncrease(): Boolean =
        windowed(2).map { (a, b) ->
            (1..3).contains(b - a)
        }.all { it }

    private fun List<Int>.safeDecrease(): Boolean =
        windowed(2).map { (a, b) ->
            (1..3).contains(a - b)
        }.all { it }

    private fun List<Int>.safeIncreaseDampened(): Boolean =
        safeIncrease() ||
                (0..size).map() { number ->
                    filterIndexed { index, _ -> index != number }.safeIncrease()
                }.any { it }

    private fun List<Int>.safeDecreaseDampened(): Boolean =
        safeDecrease() ||
                (0..size).map() { number ->
                    filterIndexed { index, _ -> index != number }.safeDecrease()
                }.any { it }
}
