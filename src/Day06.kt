import Direction.entries
import java.awt.Dimension

fun main(args: Array<String>) = dayRunner(Day06())

class Day06 : DayAdvent {

    override fun part1(input: List<String>): Any? {
        val world = parseInput(input)
        world.run()
        val exploredSum = world.exploredSum()
        return exploredSum
    }

    fun parseInput(input: List<String>): World {
        var pos = Pos(0, 0)
        var dir = Direction.NORTH

        val cave = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, ch ->
                when (ch) {
                    '#' -> 1
                    '^' -> {
                        pos = Pos(y, x); 0
                    }

                    else -> 0
                }
            }
        }.toArrayMatrix()

        return World(cave, pos, dir)
    }

    data class World(val cave: Array<Array<Int>>, var pos: Pos, var dir: Direction) {
        var finished = false
        var steps = 0
        var turns = 0
//        var explored: Array<Array<Int>> =
//            Array(cave.size) { y -> Array(cave[y].size) { x -> 0 } }.apply { setPos(pos, 1) }
        val dimentions = Pos(cave.size, cave[0].size)
        val exploredFast = IntArray(dimentions.y * dimentions.x) { 0 }.apply { setPosFast(pos, dimentions, 1) }
        //var exploredPt2: Array<Array<Int>> = exploredPt2Init()
        var exploredPt2Fast=IntArray(dimentions.y * dimentions.x) { 0 }
        var stuck = false


        private fun exploredPt2Init(): Array<Array<Int>> = Array(cave.size) { y -> Array(cave[y].size) { x -> 0 } }


        fun iterate() {
            val nextPos = pos + dir
            if (nextPos.isOutside(cave)) {
                finished = true
                return
            }
            if (pt2PosVerification(nextPos, dir)) {
                finished = true
                return
            }
            if (cave[nextPos.y][nextPos.x] == 0) {
                pos = nextPos
                steps++
                //explored.setPos(pos, 1)
                exploredFast.setPosFast(pos,dimentions, 1)
                //exploredPt2Update()
                return
            }
            dir = dir.turnRight()
            turns++
        }

        private fun pt2PosVerification(nextPos: Pos, dir1: Direction): Boolean {
//            val previousMask = exploredPt2.getVal(nextPos)
//            val updatePosMask = exploredPt2.updatePosMask(nextPos, dir1.getPosMask())
            val previousMask = exploredPt2Fast.getPosFast(nextPos, dimentions)
            val updatePosMask = previousMask or dir1.getPosMask()
            exploredPt2Fast.setPosFast(nextPos, dimentions, updatePosMask)

            if (previousMask == updatePosMask) {
                stuck = true
                return true
            }
            return false
        }

//        private fun exploredPt2Update() {
//            exploredPt2.setPos(pos, 1)
//        }

        fun run(): Boolean {
            while (!finished) {
                iterate()
            }
            return stuck
        }

        //fun exploredSum(): Int = explored.sumOf { it.sum() }
        fun exploredSum(): Int = exploredFast.sumOf { it }

        override fun equals(other: Any?): Boolean = false // NOT USED
        override fun hashCode(): Int = 0 // NOT USED

        fun deepCopy() = copy(
            cave = cave.map { it.copyOf() }.toTypedArray()
        )

    }


    /*******************************
     ***        Part 2           ***
     *******************************/
    override fun part2(input: List<String>): Any? {
        val world = parseInput(input)
        val paradoxTable = world.exploredPt2Fast.clone() // empty matrix of world
        world.cave.indices.toList().parallelStream().forEach { y ->
            world.cave[y].indices.forEach { x ->
                val testPos = Pos(y, x)
                if (world.cave.getVal(testPos) != 0) {
                    return@forEach
                }
                val testWorld = world.deepCopy().apply { cave.setPos(testPos, 1) }
                if (testWorld.run()) {
                    paradoxTable.setPosFast(testPos, world.dimentions, 1)
//                    println("Paradox at $testPos")
                }
                if (testPos == Pos(6, 3)) {
//                    println("Paradox at $testPos")
                }
            }
        }
//        printMatrix(paradoxTable)
        val sumOf = paradoxTable.sumOf { it }
        return sumOf
    }
}

enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    fun turnRight(): Direction {
        val values = entries.toTypedArray()
        return values[(ordinal + 1) % values.size]
    }

    fun getPosMask(): Int {
        return 1 shl this.ordinal // shift left by the ordinal
    }
}

data class Pos(val y: Int, val x: Int) {
    operator fun plus(direction: Direction): Pos {
        return Pos(y + direction.dy, x + direction.dx)
    }

    fun isOutside(cave: Array<Array<Int>>): Boolean {
        return y < 0 || y >= cave.size || x < 0 || x >= cave[y].size
    }
}

inline fun <reified T> List<List<T>>.toArrayMatrix(): Array<Array<T>> {
    return Array(this.size) { rowIndex ->
        this[rowIndex].toTypedArray()
    }
}

fun <T> Array<Array<T>>.setPos(pos: Pos, value: T) {
    this[pos.y][pos.x] = value
}

fun IntArray.setPosFast(pos: Pos, dim: Pos, value: Int) {
    this[dim.y*pos.y+pos.x] = value
}
fun IntArray.getPosFast(pos: Pos, dim: Pos) = this[dim.y*pos.y+pos.x]


fun <T> Array<Array<T>>.getVal(pos: Pos): T = this[pos.y][pos.x]

fun Array<Array<Int>>.updatePosMask(pos: Pos, value: Int): Int {
    this[pos.y][pos.x] = this[pos.y][pos.x] or value
    return this[pos.y][pos.x]
}





