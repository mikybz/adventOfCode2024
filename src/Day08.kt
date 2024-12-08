import kotlin.math.absoluteValue

fun main(args: Array<String>) = dayRunner(Day08())

class Day08 : DayAdvent {

    // to low: 316
    // to high: 347
    // wrong: 321
    // correct: 320
    override fun part1(input: List<String>): Any {
        val antennaMatrix = input.toArrayMatrix { it }
        val world = World(antennaMatrix)
        return world.run()
    }

    override fun part2(input: List<String>): Any = "Not implemented"

    class World(val antennaMatrix: Array<Array<Char>>) {
        val maxY: Int = antennaMatrix.size - 1
        val maxX: Int = antennaMatrix[0].size - 1
        val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        val allAntinodesByAntennaType: MutableMap<Char, Array<Array<Char>>>

        init {
            allAntennasByAntennaType = antennaMatrix.getAllAntennasByAntennaType()
            allAntinodesByAntennaType = mutableMapOf()
            printMatrix(antennaMatrix)
            println(allAntennasByAntennaType)
        }

        // val allAntennasByAntennaType: MutableMap<Char, MutableList<Pos>>
        fun run(): Int {
            val allAntinodePos = allAntennasByAntennaType.map { (antennaType, antennaPositions) ->
                val antinodePosList = antennaPositions.mapIndexed() { index, antennaPos ->
                    val subList = allAntennasByAntennaType[antennaType]!!.subList(index + 1, antennaPositions.size)
                    val subRes = createPairs(antennaPos, subList)
                    subRes

                }.flatMap { it }
                antinodePosList
            }.flatMap { it.distinct() }.distinct().sortedBy { pos -> pos.y  } //

            println("allAntinodePos:")
            println(allAntinodePos.size)

            //val allAntinodePosSet = allAntinodePos.toMutableSet()
            val allAntennaPos = allAntennasByAntennaType.values.flatten()
            //val antinodePosNotOverlappingAntennas = allAntinodePos.filter() { pos -> !allAntennaPos.contains(pos) }
            val antinodePosNotOverlappingAntennas = allAntinodePos

            var uniqueAntinodes =  antinodePosNotOverlappingAntennas.sortedBy { pos -> pos.y  }
            println("uniqueAntinodes:")
            println(uniqueAntinodes)


//            allAntennasByAntennaType.forEach { (antennaType, antennaPositions) ->
//                uniqueAntinodes.

            println("uniqueAntinodes:")
            println(uniqueAntinodes)
            println()

            antennaMatrix.indices.joinToString(separator = "", prefix = "   " ){ it.toString().padEnd(2, padChar = ' ') }.let { println(it)}

            antennaMatrix.forEachIndexed { y, line ->
                print(y.toString().padEnd(3, padChar = ' '))
                line.forEachIndexed { x, letter ->
                    //if (letter == '.') return@forEachIndexed // Next x
                    if (uniqueAntinodes.contains(Pos(y, x))) {
                        when(letter) {
                             '.' ->print("# ")
                            else ->
                                print("${letter}#")
                        }
                    } else {
                        print("${letter} ")
                    }
                }
                println()
            }

            return uniqueAntinodes.size
        }

        private fun createPairs(pos: Pos, other: MutableList<Pos>) : MutableList<Pos> {
            var pairs = mutableListOf<Pos>()
            other.forEachIndexed() { index, otherIt ->
                val dX = (otherIt.x - pos.x)
                val dY = (otherIt.y - pos.y)
                val isFirstX = (otherIt.x - pos.x) > 0
                val topYpos = if (dY > 0) pos else otherIt
                val lowYpos = if (topYpos == otherIt) pos else otherIt
                val topYposIsFirst = topYpos.x < lowYpos.x

//                val ax = if(isFirstX) {minOf(otherIt.x, pos.x)}else{maxOf(otherIt.x, pos.x}.run{ -dX}
//                val ay = minOf(otherIt.y, pos.y) - dY

                val firstPos = Pos(topYpos.y-dY.absoluteValue, if(topYposIsFirst)topYpos.x-dX.absoluteValue else topYpos.x+dX.absoluteValue)
                if (isInside(firstPos)) pairs.add( firstPos)

                val secondPos = Pos(lowYpos.y+dY.absoluteValue, if(topYposIsFirst)lowYpos.x+dX.absoluteValue else lowYpos.x-dX.absoluteValue)
                if (isInside(secondPos)) pairs.add(secondPos)
            }
            return pairs
        }


        fun isInside(pos:Pos): Boolean {
            return pos.y >= 0 && pos.y <= maxY && pos.x >= 0 && pos.x <= maxX
        }



    }


}

//private fun List<Pos>.getUnique() {
//    this.
//    TODO("Not yet implemented")
//}


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

