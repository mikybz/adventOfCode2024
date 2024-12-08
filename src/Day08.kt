import kotlin.math.absoluteValue

fun main(args: Array<String>) = dayRunner(Day08())

class Day08 : DayAdvent {

    override fun part1(input: List<String>): Any { //  320
        val antennaMatrix = input.toArrayMatrix { it }
        val world = World(antennaMatrix)
        return world.run()
    }

    override fun part2(input: List<String>): Any = "Not implemented"

    class World(val antennaMatrix: Array<Array<Char>>) {
        val maxY: Int = antennaMatrix.size - 1
        val maxX: Int = antennaMatrix[0].size - 1
        val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        var allAntinodesByAntennaType: MutableMap<Char, List<Pos>>

        init {
            allAntennasByAntennaType = antennaMatrix.getAllAntennasByAntennaType()
            allAntinodesByAntennaType = mutableMapOf()
            printMatrix(antennaMatrix)
            println(allAntennasByAntennaType)
        }

        // val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        fun run(): Int {
            // Populating allAntinodesByAntennaType
            allAntennasByAntennaType.map { (antennaType, antennaPositions) ->
                val antinodePosList = antennaPositions.mapIndexed() { index, antennaPos ->
                    val subList = allAntennasByAntennaType[antennaType]!!.subList(index + 1, antennaPositions.size)
                    val subRes = createPairs(antennaPos, subList)
                    subRes

                }.flatMap { it }
                allAntinodesByAntennaType[antennaType] = antinodePosList
            }

            val allAntinodePos = allAntinodesByAntennaType.values.flatten().distinct().sortedBy { pos -> pos.y }

            printOverlappedMatrix(antennaMatrix, allAntinodePos)

            return allAntinodePos.size
        }

        private fun createPairs(pos: Pos, other: MutableList<Pos>): MutableList<Pos> {
            var pairs = mutableListOf<Pos>()
            other.forEachIndexed() { index, otherIt ->
                val dX = (otherIt.x - pos.x).absoluteValue
                val dY = (otherIt.y - pos.y)
                val topYpos = if (dY > 0) pos else otherIt
                val lowYpos = if (topYpos == otherIt) pos else otherIt
                val topYposIsFirst = topYpos.x < lowYpos.x
                val dxFirst = if (topYposIsFirst) -dX else dX
                val firstPos = Pos(
                    topYpos.y - dY.absoluteValue,
                    topYpos.x + dxFirst
                )
                if (isInside(firstPos)) pairs.add(firstPos)

                val secondPos = Pos(
                    lowYpos.y + dY.absoluteValue,
                    lowYpos.x - dxFirst
                )
                if (isInside(secondPos)) pairs.add(secondPos)
            }
            return pairs
        }

        fun isInside(pos: Pos): Boolean {
            return pos.y >= 0 && pos.y <= maxY && pos.x >= 0 && pos.x <= maxX
        }
    }
}

private fun printOverlappedMatrix(
    matrix: Array<Array<Char>>,
    overlappedPositions: List<Pos>,
    emptyChar: Char = '.'
): Unit {
    // Print header numbers
    matrix.indices.map { if (it > 10 && it % 10 != 0) it % 10 else it }
        .joinToString(separator = "", prefix = "   ") {
            it.toString().padEnd(2, padChar = ' ')
        }.let { println(it) }

    // Print matrix, starting with row numbers, overlapped positions are marked with #
    matrix.forEachIndexed { y, line ->
        print(y.toString().padEnd(3, padChar = ' '))
        line.forEachIndexed { x, letter ->
            //if (letter == '.') return@forEachIndexed // Next x
            if (overlappedPositions.contains(Pos(y, x))) {
                when (letter) {
                    emptyChar -> print("# ")
                    else ->
                        print("${letter}#")
                }
            } else {
                print("${letter} ")
            }
        }
        println()
    }
}

private fun Array<Array<Char>>.getAllAntennasByAntennaType(): MutableMap<Char, MutableList<Pos>> {
    var groupedAntennas = mutableMapOf<Char, MutableList<Pos>>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, letter ->
            if (letter == '.') return@forEachIndexed // Next x
            groupedAntennas.insertInto(letter, Pos(y, x))
        }
    }
    return groupedAntennas
}

private fun MutableMap<Char, MutableList<Pos>>.insertInto(ch: Char, pos: Pos) {
    if (!containsKey(ch)) put(ch, mutableListOf())
    get(ch)!!.add(pos)
}

