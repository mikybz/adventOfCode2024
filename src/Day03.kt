// Exempel data p2:
// xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))

fun main(args: Array<String>) = dayRunner(Day03())

class Day03 : DayAdvent {
    val regexMulti = Regex("^mul\\((\\d+),(\\d+)\\)")

    override fun part1(input: List<String>): Any =
        input.flatMap { line ->
            line.windowed(size = 120, step = 1, partialWindows = true)
                .mapNotNull { regexMulti.find(it)?.destructured?.let { (a, b) -> a.toInt() to b.toInt() } }
        }.sumOf { it.first * it.second }


    override fun part2(input: List<String>): Any = // 76911921
        input
            .trackDebugState(reset = true)
            .flatMap { line ->
                line.windowed(size = 120, step = 1, partialWindows = true)
                    .mapNotNull { win ->
                        val nr = regexMulti.find(win)?.destructured
                            ?.let { (a, b) -> a.toInt() to b.toInt() }
                        val enable = when {
                            win.startsWith("don't()") -> false
                            win.startsWith("do()") -> true
                            else -> null
                        }
                        if (nr == null && enable == null) null
                        else nr to enable
                    }
            }
            .trackDebugState()
            .runningFold(Accumulator()) { acc, (nr, enable) ->
                when {
                    enable != null -> acc.copy(enabled = enable)
                    !acc.enabled -> acc
                    else -> acc.copy(value = acc.value + nr!!.first * nr.second)
                }
            }
            .trackDebugState()
            .last()
            .run { value }

    data class Accumulator(val enabled: Boolean = true, val value: Int = 0)
}