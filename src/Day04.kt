fun main(args: Array<String>) = dayRunner(Day04())

class Day04 : DayAdvent {
    override fun part1(input: List<String>): Any {
        return input
            .map { it.lowercase().toCharArray().toTypedArray() }
            .trackDebugState(reset = true)
            .findSubLists(Direction.values(), "xmas".length)
            .map { it.joinToString("") }
            .trackDebugState()
            .count { it == "xmas" }
    }

    // Alle "strings" med 4 i lengden fra matrisen i 8 forskjellige retninger (ikke optimalisert)
    fun <T> List<Array<T>>.findSubLists(directions: Array<Direction>, length: Int): List<List<T>> {
        val subLists = mutableListOf<List<T>>()
        for (y in 0 until this.size) {
            for (x in 0 until this[y].size) {
                for (direction in directions) {
                    val subList = mutableListOf<T>()
                    for (i in 0 until length) {
                        val yxPair = when (direction) {
                            Direction.NORTH -> y - i to x
                            Direction.EAST -> y to x + i
                            Direction.SOUTH -> y + i to x
                            Direction.WEST -> y to x - i
                            Direction.NORTH_EAST -> y - i to x + i
                            Direction.SOUTH_EAST -> y + i to x + i
                            Direction.SOUTH_WEST -> y + i to x - i
                            Direction.NORTH_WEST -> y - i to x - i
                        }
                        if (yxPair.first < 0 || yxPair.first >= this.size || yxPair.second < 0 || yxPair.second >= this[y].size) {
                            break
                        }
                        subList.add(this[yxPair.first][yxPair.second])
                    }
                    if (subList.size == length) {
                        subLists.add(subList)
                    }
                }
            }
        }
        return subLists
    }

    enum class Direction { NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST }

    /*******************************
     ***        Part 2           ***
     *******************************/
    override fun part2(input: List<String>): Any {
        return input
            .map { it.lowercase().toList() }
            .trackDebugState(reset = true)
            .findSubMatrixes("mas".length)
            .trackDebugState()
            .filterMatrix(getCompareMatrixes("mas").trackDebugState())
            .trackDebugState()
            .count()
    }

    // Returnerer alle submatriser av 3x3 for matrisen
    fun <T> List<List<T>>.findSubMatrixes(dim: Int): List<List<List<T>>> {
        val subMatrix = mutableListOf<List<List<T>>>()
        for (y in 0..this.size - dim) {
            for (x in 0..this[y].size - dim) {
                val subListY = mutableListOf<List<T>>()
                for (i in 0 until dim) {
                    val subListX = mutableListOf<T>()
                    for (j in 0 until dim) {
                        subListX.add(this[y + i][x + j])
                    }
                    subListY.add(subListX)
                }
                subMatrix.add(subListY)
            }
        }
        return subMatrix
    }

    // Returnerer 4 lister med 2D matriser som matcher "mas" i en X
    fun getCompareMatrixes(txt: String): List<Array<Array<Char?>>> {
        val n = txt.length
        // Lager 4 lister med n x n matriser
        val subMatrixes = List(4) { Array(n) { Array<Char?>(n) { null } } }

        subMatrixes.forEachIndexed { m, matrix ->
            for (i in 0 until matrix.size) {
                val z = matrix.size - i - 1 // revers i
                val charI = txt[i]
                val charZ = txt[z]
                // Ordet mas, i en X, forskjellig for hver matrise forlengs/baklengs
                matrix[i][i] = if ((m and 1) == 0) charI else charZ
                matrix[z][i] = if ((m and 2) == 0) charI else charZ
            }
        }
        return subMatrixes
    }

    // Fra en liste av submatriser, returner de som matcher minst en av filtermatrisene
    fun <T> List<List<List<T>>>.filterMatrix(filters: List<Array<Array<T?>>>): List<List<List<T>>> =
        this.filter { matrix ->
            filters.any { filter ->
                matrix.indices.all { y ->
                    matrix[y].indices.all { x ->
                        val filterValue: T? = filter[y][x]
                        filterValue == null || (matrix[y][x] == filterValue)
                    }
                }
            }
        }
}
