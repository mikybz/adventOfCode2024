fun main() = dayRunner(Day12())

class Day12 : DayAdvent {
// too high: 6067510
    override fun part1(input: List<String>): Any {
        val world: World = World(input.toCharArrayMatrix())
        world.run()
        val res = world.part1sum()
        return res
    }

    override fun part2(input: List<String>): Any {
        val world: World = World(input.toCharArrayMatrix())
        world.run()
        val res = world.part2sum()
        return res
    }

    class World(val worldMap: Array<CharArray>) {
        val dim = Pyx(y = worldMap.size, x = worldMap[0].size)
        val dividedWorld: Array<CharArray> = worldMap.map { it.clone() }.toTypedArray()
        val regions = mutableListOf<Region>()


        //println("dividedWorld=${dividedWorld.joinToString("\n")}")

        fun run() {
            //printMatrix(dividedWorld)
            worldMap.indices.forEach { y ->
                worldMap[y].indices.forEach { x ->
                    if (dividedWorld[y][x] == '#') return@forEach // Next x
                    regions.add(
                        Region(Pyx(y, x))
                    )
                }
            }
            println()
        }

        fun part1sum(): Int {
            return regions.sumOf { reg-> reg.sumPart1() }
        }
        fun part2sum(): Int {
            return regions.sumOf { reg-> reg.sumPart2() }
        }


        inner class Region(val startPos: Pyx) {
            val regionWorld = Array(dim.y) { CharArray(dim.x) { '#' } }
            val posList = mutableListOf<Pyx>()
            val charx: Char

            init {
                charx = worldMap.get(startPos)
                recursiveAdd(startPos)
                //printMatrix(worldMap)
                //println()
            }

            private fun recursiveAdd(pyx: Pyx) {
                posList.add(pyx)
                regionWorld.set(pyx, charx)
                dividedWorld.set(pyx, '#')
                pyx.get4Neighbours().forEach { neighbour ->
                    if (neighbour.isOutside(dim)) return@forEach
                    val nVal =worldMap.get(neighbour)
                    if(nVal!=charx)  return@forEach
                    if (regionWorld.get(neighbour)!='#') return@forEach
                    //if (dividedWorld[neighbour.y][neighbour.x] == '#') return@forEach
                    recursiveAdd(neighbour)
                }
            }

            fun addPos(pos: Pyx) {
                if (dividedWorld[pos.y][pos.x] == '#') return
                posList.add(pos)
            }

            fun sumPart1(): Int {
                val area = posList.size
                val edges = posList.sumOf { getEdges(it) }
               // println("Region ${charx} area: ${area} edges: ${edges} price: ${ area * edges} ")
                return area * edges
            }

            fun getEdges(pyx: Pyx): Int {
                val edges = pyx.get4Neighbours().count {
                    it.isOutside(dim) || regionWorld.get(it) != charx }
                return edges
            }

            fun sumPart2(): Int {
                val area = posList.size

                val edges = getEdges2()
                //println("Region ${charx} area: ${area} edges: ${edges} price: ${ area * edges} ")
                return area * edges
            }
            fun getEdges2(): Int {
                val start = PosDir(startPos, Directions.NORTH)
                var currentPos = start
                var edges = 0
                while(true) {
                    val checkTurnLeftPos = currentPos.leftTurnPossible()



                posList.forEach { pos ->
                    val edge = pos.get4Neighbours().count {
                        it.isOutside(dim) || regionWorld.get(it) != charx }
                    edges += edge
                }
                return edges

            }
            data class PosDir(val pos: Pyx, val dir: Directions)

        }

    }



}

private inline fun Array<CharArray>.get(pyx: Pyx): Char = this[pyx.y][pyx.x]
private inline fun Array<CharArray>.set(pyx: Pyx, value: Char) {
    this[pyx.y][pyx.x] = value
}

