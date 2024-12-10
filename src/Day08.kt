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
        val allAntennasByAntennaType: MutableMap<Char, MutableList<Pyx>>
        var allAntinodesByAntennaType: MutableMap<Char, List<Pyx>>

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
        private fun createAntinodesFromAntennaPairsPart1(pyx: Pyx, other: MutableList<Pyx>): MutableList<Pyx> {
            var pairs = mutableListOf<Pyx>()
            other.forEachIndexed { index, otherIt ->
                val dX = (otherIt.x - pyx.x).absoluteValue
                val dY = (otherIt.y - pyx.y)
                val topYpos = if (dY > 0) pyx else otherIt
                val lowYpos = if (topYpos == otherIt) pyx else otherIt
                val topYposIsFirst = topYpos.x < lowYpos.x
                val dxFirst = if (topYposIsFirst) -dX else dX
                val firstPyx = Pyx(
                    topYpos.y - dY.absoluteValue,
                    topYpos.x + dxFirst
                )
                if (isInsideMatrix(firstPyx)) pairs.add(firstPyx)

                val secondPyx = Pyx(
                    lowYpos.y + dY.absoluteValue,
                    lowYpos.x - dxFirst
                )
                if (isInsideMatrix(secondPyx)) pairs.add(secondPyx)
            }
            return pairs
        }

        // Create N antinodes for each antenna pairs, pairing input pos with every element in other
        private fun createAntinodesFromAntennaPairsPart2(pyx: Pyx, other: MutableList<Pyx>): MutableList<Pyx> {
            var pairs = mutableListOf<Pyx>()
            other.forEachIndexed { index, otherIt ->
                val dX = (otherIt.x - pyx.x).absoluteValue
                val dY = (otherIt.y - pyx.y)
                val topYpos = if (dY > 0) pyx else otherIt
                val lowYpos = if (topYpos == otherIt) pyx else otherIt
                val topYposIsFirst = topYpos.x < lowYpos.x
                val dxNext = if (topYposIsFirst) dX else -dX
                // Iterate down from top antenna
                var itPos = topYpos
                while(true) {
                    pairs.add(itPos)
                    itPos = Pyx(itPos.y + dY, itPos.x + dxNext)
                    if (!isInsideMatrix(itPos)) break
                }
                // Iterate up from top antenna
                itPos = topYpos
                while(true) {
                    itPos = Pyx(itPos.y - dY, itPos.x - dxNext)
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

        fun isInsideMatrix(pyx: Pyx): Boolean {
            return pyx.y >= 0 && pyx.y <= maxY && pyx.x >= 0 && pyx.x <= maxX
        }

        private fun Array<Array<Char>>.getAllAntennasByAntennaType(): MutableMap<Char, MutableList<Pyx>> {
            var groupedAntennas = mutableMapOf<Char, MutableList<Pyx>>()
            forEachIndexed { y, line ->
                line.forEachIndexed { x, letter ->
                    if (letter == '.') return@forEachIndexed // Next x, skipping all '.'
                    groupedAntennas.insertInto(letter, Pyx(y, x))
                }
            }
            return groupedAntennas
        }
    }
}






