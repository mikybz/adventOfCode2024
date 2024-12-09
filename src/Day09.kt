fun main() = dayRunner(Day09())

class Day09 : DayAdvent {
    // To low: 1178849310
    override fun part1(input: List<String>): Any {
        val fsBlocks = input.parseToFilesystem()
        compact(fsBlocks)
        return calculate(fsBlocks)
    }
    override fun part2(input: List<String>): Any {
        val fsInfo = input.parseToFilesystemAndBlocklist()
        compactNonFragmented(fsInfo)
        return calculate(fsInfo)
    }



    private fun compact(fsBlocks: Array<Int>) {
        var front = 0
        var back = fsBlocks.size - 1
        while (true) {
            while (fsBlocks[front] != -1) front++
            while (fsBlocks[back] == -1 && back > front) back--
            if (front >= back) break
            fsBlocks[front] = fsBlocks[back]
            fsBlocks[back] = -1
        }
    }
    data class FsInfo(val fileSystemBlocks: Array<Int>, val blockList: MutableList<Block>, val fileList: MutableList<Block>){}

    private fun compactNonFragmented(fsInfo: FsInfo): Int {
        val (fileSystemBlocks,  blockList, fileList) = fsInfo
        fileList.reversed().forEachIndexed() { index, file ->

            val emptyBlockIndex = blockList.indexOfFirst() { it.file == -1 && it.size >= file.size }
            if (emptyBlockIndex != -1) {
                val emptyblock = blockList[emptyBlockIndex]
                blockList.removeAt(emptyBlockIndex)
                blockList.remove(file)
                blockList.add(emptyBlockIndex, Block(file.size, file.file))
                val remaining = emptyblock.size - file.size
                if (remaining > 0) {
                    blockList.add(emptyBlockIndex + 1, Block(remaining, -1))
                }
                println(blockList)
                blockList
            }
        }
        return 0

    }

    private fun calculate(fsBlocks: Array<Int>): Long =
        fsBlocks.foldIndexed(0L) { index, acc, block ->
            if(block == -1) return acc
            acc + (index * block.toLong())
        }

    private fun calculate(info: FsInfo): Any {
        return info.blockList.foldIndexed(0 to 0L) { index, (realIndex, acc: Long), block ->
            val newRealIndex = realIndex + block.size
            if(block.file == -1) {
                Pair(newRealIndex, acc)
            } else {
                var accNew = acc
                (realIndex..newRealIndex).forEach { currentIndex ->
                    accNew += currentIndex * block.file
                }
                Pair(newRealIndex, acc)
            }
        }

    }



    fun List<String>.parseToFilesystem(): Array<Int> {
        val fileSystemLength = first().sumOf { it - '0' }
        val fileSystemBlocks = Array<Int>(fileSystemLength) { -1 }
        first().foldIndexed(0) { index, pos, block ->
            val blockSize = block - '0'
            if (index % 2 == 0) repeat(blockSize) {
                fileSystemBlocks[pos + it] = index / 2
            }
            pos + blockSize
        }
        return fileSystemBlocks
    }
    class Block(var size: Int, val file: Int){
        fun isFree() = file == -1
        override fun toString() =  (if (file == -1) "X" else file.toString()) + "($size)"
    }


    fun List<String>.parseToFilesystemAndBlocklist(): FsInfo {
        val fileSystemLength = first().sumOf { it - '0' }
        val blockList= mutableListOf<Block>()
        val fileList= mutableListOf<Block>()
        val fileSystemBlocks = Array<Int>(fileSystemLength) { -1 }
        first().foldIndexed(0) { index, pos, block ->
            val blockSize = block - '0'
            val isFile = index % 2 == 0
            val file = when (isFile) {true -> index / 2 ; false -> -1}
            val block = Block(blockSize, file)
            blockList.add(block)
            if (isFile) {
                fileList.add(block)
                repeat(blockSize) {
                    fileSystemBlocks[pos + it] = file
                }
            }
            pos + blockSize
        }
        return FsInfo(fileSystemBlocks, blockList, fileList)
    }

    private fun printFs(fileSystemBlocks: Array<Int>) {
        val fs = fileSystemBlocks.map {
            when (it) {
                -1 -> '.'
                else -> it
            }
        }.joinToString("")
        println(fs)
    }

//        map { line ->
//        line.split("  ", limit=2)
//            .map(String::trim)
//            .let { (a, b) -> a.toInt() to b.toInt() }
//    }
}