import kotlin.enums.EnumEntries

fun main() = dayRunner(Day04())

class Day04 : DayAdvent {
    override fun part1(input: List<String>): Any {
        return input
            .map { it.lowercase().toCharArray().toTypedArray() }
            .findSubLists2(Directions.entries, "xmas")
    }

    fun <T> List<Array<T>>.findSubLists2(directions: EnumEntries<Directions>, txt: String): Int {
        val matrixLength = this.size
        val txtLength = txt.length
        var count = 0
        for (y in 0 until matrixLength) {
            for (x in 0 until this[y].size) {
                dir@for (direction in directions) {
                    for (i in 0 until txtLength) {
                        val (outY, outX) = Pos(y, x) + direction.pos * i
                        if (outY < 0 || outY >= matrixLength || outX < 0 || outX >= this[y].size) {
                            continue@dir
                        }
                        if (this[outY][outX]!=txt[i]) {
                            continue@dir
                        }
                    }
                    count++
                }
            }
        }
        return count
    }


    /*******************************
     ***        Part 2           ***
     *******************************/
    override fun part2(input: List<String>): Any {
        return input
            .map { it.lowercase().toList() }
            .findSubMatrixes("mas".length)
            .filterMatrix(getCompareMatrixes("mas"))
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
