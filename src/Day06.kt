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
        var pyx = Pyx(0, 0)
        var dir = Direction.NORTH

        val cave = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, ch ->
                when (ch) {
                    '#' -> 1
                    '^' -> {
                        pyx = Pyx(y, x); 0
                    }

                    else -> 0
                }
            }
        }.toArrayMatrix()

        return World(cave, pyx, dir)
    }

    data class World(val cave: Array<Array<Int>>, var pyx: Pyx, var dir: Direction) {
        var finished = false
        var steps = 0
        var turns = 0
        val startPos = pyx
        val dimensions = Pyx(cave.size, cave[0].size)
        var exploredPt2Fast = createFastMatrix(dimensions)
        var stuck = false
        var testPyx: Pyx? = null

        fun iterate() {
            val nextPos = pyx + dir
            if (nextPos.isOutside(cave)) {
                finished = true
                return
            }
            if (pt2PosVerification(nextPos, dir)) {
                finished = true
                return
            }
            if (cave[nextPos.y][nextPos.x] == 0 && !crashedInTestPos(nextPos)) {
                pyx = nextPos
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
        private fun crashedInTestPos(pyx: Pyx): Boolean {
            return testPyx?.let {
                it == pyx
            } ?: false
        }

        private fun pt2PosVerification(nextPyx: Pyx, dir1: Direction): Boolean {
            val previousMask = exploredPt2Fast.getPosFast(nextPyx, dimensions)
            val updatePosMask = previousMask or dir1.getPosMask()
            exploredPt2Fast.setPosFast(nextPyx, dimensions, updatePosMask)

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
            val testPyx = Pyx(y, x)
            if (world.cave.getVal(testPyx) != 0) {
                return@forEach
            }
            val testWorld = world.copy()
            testWorld.testPyx = testPyx
            if (testWorld.run()) {
                paradoxTable.setPosFast(testPyx, world.dimensions, 1)
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

