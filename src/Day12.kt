fun main() = dayRunner(Day12())

class Day12 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val world: World = World(input.toCharArrayMatrix())
        world.run()
        return "Not implemented"
    }

    class World(val input: Array<CharArray>) {
        val dim = Pyx(y = input.size, x = input[0].size)
        val dividedWorld: Array<CharArray> = input.map { it.clone() }.toTypedArray()
        val regions = mutableListOf<Region>()


        //println("dividedWorld=${dividedWorld.joinToString("\n")}")

        fun run() {
            printMatrix(dividedWorld)
            input.indices.forEach { y ->
                input[y].indices.forEach { x ->
                    if (dividedWorld[y][x] == '#') return@forEach // Next x
                    regions.add(
                        Region(input[y][x]).apply {
                            dividedWorld[y][x] = '#'
                            addPos(Pyx(y, x))
                        })

                }
            }
        }


        inner class Region(val char: Char, val startPos: Pyx) {
            val myWorld = Array(dim.y) { Array(dim.x) { 0 } }
            val posList = mutableListOf<Pyx>()
            init {

                recursiveAdd(startPos)
            }

            private fun recursiveAdd(pyx: Pyx) {
                posList.add(pyx)
                myWorld[pyx.y][pyx.x] = char
            }

            fun addPos(pos: Pyx) {
                if(dividedWorld[pos.y][pos.x] == '#') return
                posList.add(pos)
            }

        }

    }


    override fun part2(input: List<String>): Any {
        return "Not implemented"
    }
}