import Direction.entries

fun main() = dayRunner(Day06())

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
        val startPos = pos
        val dimensions = Pos(cave.size, cave[0].size)
        var exploredPt2Fast = createFastMatrix(dimensions)
        var stuck = false
        var testPos: Pos? = null

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
            if (cave[nextPos.y][nextPos.x] == 0 && !crashedInTestPos(nextPos)) {
                pos = nextPos
                steps++
                //exploredFast.setPosFast(pos, dimensions, 1)
                return
            }
            exploredPt2Fast.setPosFast(nextPos, dimensions, 0)
            dir = dir.turnRight()
            turns++
        }

        // For part 2 we do not want add the testItem to the collition detection matrix
        // since then we have to deepcopy the world, to allow parallel processing
        // Instead we have a separate check for it
        private fun crashedInTestPos(pos: Pos): Boolean {
            return testPos?.let {
                it == pos
            } ?: false
        }

        private fun pt2PosVerification(nextPos: Pos, dir1: Direction): Boolean {
            val previousMask = exploredPt2Fast.getPosFast(nextPos, dimensions)
            val updatePosMask = previousMask or dir1.getPosMask()
            exploredPt2Fast.setPosFast(nextPos, dimensions, updatePosMask)

            if (previousMask == updatePosMask) {
                stuck = true
                return true
            }
            return false
        }

        fun run(): Boolean {
            while (!finished) {
                iterate()
            }
            exploredPt2Fast.setPosFast(startPos, dimensions, 0xff)
            return stuck
        }

        fun exploredSum(): Int =
            exploredPt2Fast.filter { it != 0 }.size

        override fun equals(other: Any?): Boolean = false // not used, but needed for the compiler
        override fun hashCode(): Int = 0                  // same here
    }


    fun part2Inner(y: Int, world: World, paradoxTable: IntArray) {
        world.cave[y].indices.forEach { x ->
            val testPos = Pos(y, x)
            if (world.cave.getVal(testPos) != 0) {
                return@forEach
            }
            val testWorld = world.copy()
            testWorld.testPos = testPos
            if (testWorld.run()) {
                paradoxTable.setPosFast(testPos, world.dimensions, 1)
            }
        }
    }

    /*******************************
     ***        Part 2           ***
     *******************************/
    override fun part2(input: List<String>): Any? {
        val world: World = parseInput(input)
        val paradoxTable: IntArray = world.exploredPt2Fast.clone() // empty matrix of world
        if (globalMultiThreadEnabled) {
            world.cave.indices.toList().parallelStream().forEach { y ->
                //processInParallel<Int, Unit>(world.cave.indices.toList()) { y ->
                part2Inner(y, world, paradoxTable)
            }
        } else {
            world.cave.indices.toList().forEach { y ->
                part2Inner(y, world, paradoxTable)
            }
        }
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

