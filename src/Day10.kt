fun main() = dayRunner(Day10())

class Day10 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val matrix = input.toArrayMatrix { it }
        val nPaths = countPaths(matrix)
        return "Not implemented"
    }

    private fun countPaths(matrix: Array<Array<Char>>) {
        var validPosList = startPositions(matrix)
        println("Start positions: $validPosList")
        for(nextNr in '1' .. '9') {
            println("Iteration $nextNr")
            validPosList = validPosList.flatMap { pos: Pyx ->
                pos.get4Neighbours()
                    .filter { neighbour -> !neighbour.isOutside(matrix) && matrix.getVal(neighbour)==nextNr }
            }.distinct()
            println("Valid positions: $validPosList")
            println("Nr of valid positions: ${validPosList.size}")
        }
        printMatrix(matrix)
        println("done")

//        validPosList.map { startPos ->
//            val nextPos='1'
//            startPos.get4Neighbours().filter { !it.isOutside(matrix) && matrix.getVal(it)==nextPos }.forEach { println(it) }
//        }

    }

    private fun startPositions(matrix: Array<Array<Char>>) = matrix.mapIndexed { y, row ->
        row.mapIndexed { x, cell ->
            if (cell == '0') Pyx(y, x) else null
        }
    }.flatten().filterNotNull()


    override fun part2(input: List<String>): Any {
        return "Not implemented"
    }
}

private fun Pyx.get4Neighbours(): List<Pyx> = listOf<Pyx>(Pyx(y - 1, x), Pyx(y + 1, x), Pyx(y, x - 1), Pyx(y, x + 1))


