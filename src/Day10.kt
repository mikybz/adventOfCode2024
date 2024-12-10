fun main() = dayRunner(Day10())

class Day10 : DayAdvent {

    override fun part1(input: List<String>): Any { // 733
        val matrix = input.toArrayMatrix { it }
        val nPaths = countPaths(matrix)
        return nPaths
    }

    private fun countPaths(matrix: Array<Array<Char>>): Int {
        var validPosList = startPositions(matrix)
//        println("Start positions: $validPosList")
        val result = validPosList.sumOf { pos ->
            iterateToPos(pos, matrix)
        }
//        println("Sum of: $result")
        return result
    }

    private fun iterateToPos(startPos: Pyx, matrix: Array<Array<Char>>): Int {

        var validPosList = listOf<Pyx>(startPos)
        for (nextNr in '1'..'9') {
//            println("Iteration $nextNr")
            validPosList = validPosList.flatMap { pos: Pyx ->
                pos.get4Neighbours()
                    .filter { neighbour -> !neighbour.isOutside(matrix) && matrix.getVal(neighbour) == nextNr }
            }.distinct()
//            println("Valid positions: $validPosList")
//            println("Nr of valid positions: ${validPosList.size}")
        }
        return validPosList.size
    }

    private fun startPositions(matrix: Array<Array<Char>>) = matrix.mapIndexed { y, row ->
        row.mapIndexed { x, cell ->
            if (cell == '0') Pyx(y, x) else null
        }
    }.flatten().filterNotNull()


    override fun part2(input: List<String>): Any { // 733
        val matrix = input.toArrayMatrix { it }
        val nPaths = countPaths2(matrix)
        return nPaths
    }

    private fun countPaths2(matrix: Array<Array<Char>>): Int {
        var validPosList = startPositions(matrix)
//        println("Start positions: $validPosList")
        val result = validPosList.sumOf { pos ->
            iterateToPos2(pos, matrix)
        }
//        println("Sum of: $result")
        return result
    }

    private fun iterateToPos2(startPos: Pyx, matrix: Array<Array<Char>>): Int {

        var validPosList = listOf<Pyx>(startPos)
        for (nextNr in '1'..'9') {
//            println("Iteration $nextNr")
            validPosList = validPosList.flatMap { pos: Pyx ->
                pos.get4Neighbours()
                    .filter { neighbour -> !neighbour.isOutside(matrix) && matrix.getVal(neighbour) == nextNr }
            }
//            println("Valid positions: $validPosList")
//            println("Nr of valid positions: ${validPosList.size}")
        }
        return validPosList.size
    }
}

private fun Pyx.get4Neighbours(): List<Pyx> = listOf<Pyx>(Pyx(y - 1, x), Pyx(y + 1, x), Pyx(y, x - 1), Pyx(y, x + 1))


