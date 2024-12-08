import kotlin.math.absoluteValue

fun main() = dayRunner(Day08())

class Day08 : DayAdvent {

    override fun part1(input: List<String>): Any { //  320
        val antennaMatrix = input.toArrayMatrix { it }
        val world = World(antennaMatrix)
        return world.run1()
    }

    override fun part2(input: List<String>): Any {
        val antennaMatrix = input.toArrayMatrix { it }
        val world = World(antennaMatrix)
        return world.run2()
    }

    class World(val antennaMatrix: Array<Array<Char>>) {
        val maxY: Int = antennaMatrix.size - 1
        val maxX: Int = antennaMatrix[0].size - 1
        val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        var allAntinodesByAntennaType: MutableMap<Char, List<Pos>>

        init {
            allAntennasByAntennaType = antennaMatrix.getAllAntennasByAntennaType()
            allAntinodesByAntennaType = mutableMapOf()
            if (globalDebugPrint) {
                printMatrix(antennaMatrix)
                println(allAntennasByAntennaType)
            }
        }

        // val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        fun run1(): Int {
            // Populating allAntinodesByAntennaType
            allAntennasByAntennaType.map { (antennaType, antennaPositions) ->
                val antinodePosList = antennaPositions.mapIndexed { index, antennaPos ->
                    val subList = allAntennasByAntennaType[antennaType]!!.subList(index + 1, antennaPositions.size)
                    val subRes = createAntinodesFromAntennaPairsPart1(antennaPos, subList)
                    subRes
                }.flatMap { it }
                allAntinodesByAntennaType[antennaType] = antinodePosList
            }

            val allAntinodePos = allAntinodesByAntennaType.values.flatten().distinct().sortedBy { pos -> pos.y }

            if (globalDebugPrint) printOverlappedMatrix(antennaMatrix, allAntinodePos)

            return allAntinodePos.size
        }

        // Create 2 antinodes for each antenna pairs, pairing input pos with every element in other
        private fun createAntinodesFromAntennaPairsPart1(pos: Pos, other: MutableList<Pos>): MutableList<Pos> {
            var pairs = mutableListOf<Pos>()
            other.forEachIndexed { index, otherIt ->
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
                if (isInsideMatrix(firstPos)) pairs.add(firstPos)

                val secondPos = Pos(
                    lowYpos.y + dY.absoluteValue,
                    lowYpos.x - dxFirst
                )
                if (isInsideMatrix(secondPos)) pairs.add(secondPos)
            }
            return pairs
        }

        // Create N antinodes for each antenna pairs, pairing input pos with every element in other
        private fun createAntinodesFromAntennaPairsPart2(pos: Pos, other: MutableList<Pos>): MutableList<Pos> {
            var pairs = mutableListOf<Pos>()
            other.forEachIndexed { index, otherIt ->
                val dX = (otherIt.x - pos.x).absoluteValue
                val dY = (otherIt.y - pos.y)
                val topYpos = if (dY > 0) pos else otherIt
                val lowYpos = if (topYpos == otherIt) pos else otherIt
                val topYposIsFirst = topYpos.x < lowYpos.x
                val dxNext = if (topYposIsFirst) dX else -dX
                // Iterate down from top antenna
                var itPos = topYpos
                while(true) {
                    pairs.add(itPos)
                    itPos = Pos(itPos.y + dY, itPos.x + dxNext)
                    if (!isInsideMatrix(itPos)) break
                }
                // Iterate up from top antenna
                itPos = topYpos
                while(true) {
                    itPos = Pos(itPos.y - dY, itPos.x - dxNext)
                    if (!isInsideMatrix(itPos)) break
                    pairs.add(itPos)
                }
            }
            return pairs
        }

        fun run2(): Int {
            // Populating allAntinodesByAntennaType
            allAntennasByAntennaType.map { (antennaType, antennaPositions) ->
                val antinodePosList = antennaPositions.mapIndexed { index, antennaPos ->
                    val subList = allAntennasByAntennaType[antennaType]!!.subList(index + 1, antennaPositions.size)
                    createAntinodesFromAntennaPairsPart2(antennaPos, subList)
                }.flatMap { it }
                allAntinodesByAntennaType[antennaType] = antinodePosList
            }

            val allAntinodePos = allAntinodesByAntennaType.values.flatten().distinct().sortedBy { pos -> pos.y }

            if (globalDebugPrint) printOverlappedMatrix(antennaMatrix, allAntinodePos)

            return allAntinodePos.size
        }

        fun isInsideMatrix(pos: Pos): Boolean {
            return pos.y >= 0 && pos.y <= maxY && pos.x >= 0 && pos.x <= maxX
        }

        private fun Array<Array<Char>>.getAllAntennasByAntennaType(): MutableMap<Char, MutableList<Pos>> {
            var groupedAntennas = mutableMapOf<Char, MutableList<Pos>>()
            forEachIndexed { y, line ->
                line.forEachIndexed { x, letter ->
                    if (letter == '.') return@forEachIndexed // Next x, skipping all '.'
                    groupedAntennas.insertInto(letter, Pos(y, x))
                }
            }
            return groupedAntennas
        }
    }
}






