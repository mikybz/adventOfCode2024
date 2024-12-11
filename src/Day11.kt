fun main() = dayRunner(Day11())

// to low: 172447
// to high: 262660
class Day11 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val parseNr: List<Long> = input.parseNr()
        val sum: Long = parseNr.sumOf { stoneSum(it, 25) }
        println("Sum: $sum")
        return sum
    }

    private fun splitNr(nr: Long): Pair<Long, Long> {
        val strNr = nr.toString()
        val half = strNr.length / 2
        val firstHalf = strNr.substring(0, half).toLong()
        val secondHalf = strNr.substring(half).toLong()
        return firstHalf to secondHalf
    }

    private fun stoneSum(nr: Long, iterationsLeft: Int): Long {
        if (iterationsLeft == 0) return 1
        val nextIteration = iterationsLeft - 1
        return when  {
            nr == 0L -> stoneSum(1, nextIteration)
            nr > 10L && (nr.toString().length % 2) == 0 -> splitNr(nr).toList()
                .sumOf {
                    //println("nr:$nr it:$it");
                    stoneSum(it,nextIteration) }
            else -> stoneSum(nr * 2024, nextIteration)
        }
    }

    private fun List<String>.parseNr(): List<Long> {
        return first().split("\\s+".toRegex()).map { it.toLong() }
    }

    override fun part2(input: List<String>): Any =
        "not implemented"
}