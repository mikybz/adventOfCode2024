fun main() = dayRunner(Day09())

class Day09 : DayAdvent {
    data class FsInfo(val fileSystemBlocks: Array<Int>, val blockList: MutableList<Block>, val fileList: MutableList<Block>)

    override fun part1(input: List<String>): Any {
        val fsBlocks = input.parseToFilesystem()
        compact(fsBlocks)
        return calculate(fsBlocks)
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

    private fun calculate(fsBlocks: Array<Int>): Long =
        fsBlocks.foldIndexed(0L) { index, acc, block ->
            if(block == -1) return acc
            acc + (index * block.toLong())
        }


    override fun part2(input: List<String>): Any {
        val fsInfo = input.parseToFilesystemAndBlocklist()
        compactNonFragmented(fsInfo)
        return calculate(fsInfo)
    }








    private fun compactNonFragmented(fsInfo: FsInfo) {
        val (fileSystemBlocks,  blockList, fileList) = fsInfo
        fileList.reversed().forEachIndexed() { index, file ->

            val emptyBlockIndex = blockList.indexOfFirst() { it.file == -1 && it.size >= file.size }
            val fileIndex = blockList.indexOf(file)

            if (emptyBlockIndex>fileIndex)
                return@forEachIndexed

            if (emptyBlockIndex != -1) {

                blockList[fileIndex] = Block(file.size, -1)
//                println("Mov $file from $fileIndex to $emptyBlockIndex")

                val emptyblock = blockList[emptyBlockIndex]

//                blockList.remove(file)
//                blockList.add(emptyBlockIndex, Block(file.size, file.file))

                //blockList.removeAt(emptyBlockIndex)
                blockList[emptyBlockIndex] = file

                val remaining = emptyblock.size - file.size
                if (remaining > 0) {
                    blockList.add(emptyBlockIndex + 1, Block(remaining, -1))
                }
                //println(blockList)
                mergeEmptyBlocks(blockList)
//                println(blockList)
                blockList
            }
        }
    }

    private fun mergeEmptyBlocks(blocks: MutableList<Block>) {
        var max :Int = blocks.size-1
        var i = 0
        do {
            if(blocks[i].isFree() && blocks[i + 1].isFree()) {
                blocks[i].size += blocks[i + 1].size
                blocks.removeAt(i + 1)
//                println("Merged empty $i with next")
                max--
            } else {
                i++
            }
        } while (i<max)
    }



    private fun calculate(info: FsInfo): Any {
        val resultat = info.blockList.foldIndexed(0 to 0L) { index, (realIndex, acc: Long), block ->
            val newRealIndex = realIndex + block.size
            if (block.file == -1) {
                Pair(newRealIndex, acc)
            } else {
                var accNew = acc
                (realIndex..newRealIndex-1).forEach { currentIndex ->
                    val extra = currentIndex * block.file
                    accNew += extra
                    accNew
                }
                Pair(newRealIndex, accNew)
            }
        }
        return resultat.second

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
}