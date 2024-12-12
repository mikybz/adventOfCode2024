fun main() = dayRunner(Day11())

class Day11 : DayAdvent {
    override fun part1(input: List<String>): Any {
        val parseNr: List<Long> = input.parseNr()
        val sum: Long = parseNr.sumOf { stoneSum(it, 25) }
        return sum
    }

    override fun part2(input: List<String>): Any {
        val parseNr: List<Long> = input.parseNr()
        val sum: Long = parseNr.sumOf { stoneSum(it, 75) }
        stoneResultMap = HashMap()
        return sum
    }

    private fun List<String>.parseNr(): List<Long> = first().split("\\s+".toRegex()).map { it.toLong() }

    private fun stoneSum(nr: Long, iterationsLeft: Int): Long {
        val key = StoneSum(nr, iterationsLeft)
        stoneResultMap[key]?.let { return it }
        if (iterationsLeft == 0) return 1

        val nextIteration = iterationsLeft - 1
        val sum = when {
            nr == 0L -> stoneSum(1, nextIteration)
            evenDigits(nr) -> splitNr(nr).sumOf { stoneSum(it, nextIteration) }
            else -> stoneSum(nr * 2024, nextIteration)
        }
        stoneResultMap[key] = sum
        return sum
    }

    private inline fun evenDigits(nr: Long): Boolean = nr >= 10L && (nr.toString().length % 2) == 0

    private fun splitNr(nr: Long): List<Long> {
        val strNr = nr.toString()
        val half = strNr.length / 2
        val firstHalf = strNr.substring(0, half).toLong()
        val secondHalf = strNr.substring(half).toLong()
        return listOf<Long>(firstHalf, secondHalf)
    }

    var stoneResultMap: HashMap<StoneSum, Long> = HashMap()

    data class StoneSum(val nr: Long, val iterationsLeft: Int)
}