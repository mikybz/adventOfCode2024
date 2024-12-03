// Exempel data p2:
// xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))

fun main(args: Array<String>) = dayRunner(Day03())

class Day03 : DayAdvent {
    val regexMulti = Regex("^mul\\((\\d+),(\\d+)\\)")
    val debugStates = mutableListOf<Any>()

    override fun part1(input: List<String>): Any = // 163931492
        input.flatMap { line ->
            line.windowed(size = 120, step = 1, partialWindows = true)
                .mapNotNull { regexMulti.find(it)?.destructured?.let { (a, b) -> a.toInt() to b.toInt() } }
        }.sumOf { it.first * it.second }

    // Correct: 76911921
    override fun part2(input: List<String>): Any =
        input
            .also { state ->
                debugStates.clear()
                debugStates.add(state)
            }
            .flatMap { line ->
                line.windowed(size = 120, step = 1, partialWindows = true)
                    .map { win ->
                        Pair(
                            regexMulti.find(win)?.groupValues
                                ?.let { it[1].toInt() to it[2].toInt() },
                            when {
                                win.startsWith("don't()") -> false
                                win.startsWith("do()") -> true
                                else -> null
                            }
                        )
                    }
                    .filter { it.first != null || it.second != null }
            }
            .also { state -> debugStates.add(state) }
            .runningFold(Accumulator()) { acc, (match, enabled) ->
                when {
                    (enabled != null) -> acc.copy(enabled = enabled)
                    (!acc.enabled) -> acc
                    else -> acc.copy(value = acc.value + match!!.first * match.second)
                }
            }
            .also { state -> debugStates.add(state) }
            .last()
            .run { value }

    data class Accumulator(val enabled: Boolean = true, val value: Int = 0)
}
