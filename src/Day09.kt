fun main() = dayRunner(Day09())

class Day09 : DayAdvent {

    override fun part1(input: List<String>): Any {
        val fsBlocks = input.parseToFilesystem()
        compactFs(fsBlocks)
        return calculate(fsBlocks)
    }

    fun List<String>.parseToFilesystem(): Array<Int> {
        val totalNrOfBlocks = first().sumOf { it.digitToInt() }
        val blockArray = Array<Int>(totalNrOfBlocks) { -1 }

        first().foldIndexed(0) { index, pos, inputSize ->
            val blockSize = inputSize.digitToInt()
            if (index % 2 == 0) (pos until pos + blockSize).forEach { //repeat(blockSize)
                blockArray[it] = index / 2
            }
            pos + blockSize // next pos accumulator
        }
        return blockArray
    }

    class Block(var size: Int, val fileId: Int) {
        fun isFree() = fileId == -1
        override fun toString(): String {
            return (if (fileId == -1) "X" else fileId.toString()) + "($size)"
        }
    }

    private fun compactFs(fsBlocks: Array<Int>) {
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
            if (block == -1) return acc
            acc + (index * block.toLong())
        }


    /*******************************
     ***        Part 2           ***
     *******************************/

    override fun part2(input: List<String>): Any {
        val fsInfo = input.parseToFilesystemAndBlocklist()
        compactNonFragmented(fsInfo)
        return calculate(fsInfo)
    }

    data class FsInfo(val blockList: MutableList<Block>, val maxFileId: Int)

    fun List<String>.parseToFilesystemAndBlocklist(): FsInfo {
        val blockList = mutableListOf<Block>()
        val maxFile = first().length / 2

        first().foldIndexed(0) { index, pos, block ->
            val blockSize = block.digitToInt()
            val isFile = index % 2 == 0
            val fileId = when (isFile) {
                true -> index / 2; false -> -1
            }
            blockList.add(Block(blockSize, fileId))
            pos + blockSize
        }
        return FsInfo(blockList, maxFile)
    }

    // Compacting blockList in fsInfo
    private fun compactNonFragmented(fsInfo: FsInfo) {
        val (blockList, maxFile) = fsInfo
        for (fileId in maxFile downTo 0) {
            val fileIndex = blockList.indexOfFirst { block -> block.fileId == fileId }
            val file = blockList[fileIndex]
            val emptyBlockIndex = blockList.indexOfFirst() { it.fileId == -1 && it.size >= file.size }
            // Continue on no space, or space is after file
            if (emptyBlockIndex == -1 || emptyBlockIndex > fileIndex)
                continue

            // Replace file with empty block
            blockList[fileIndex] = Block(file.size, -1)
            // Replace emptyBlock with file
            val emptyBlockSize = blockList[emptyBlockIndex].size
            blockList[emptyBlockIndex] = file
            // If the empty block is larger than the file, reinsert the remaining space
            val remaining = emptyBlockSize - file.size
            if (remaining > 0) {
                blockList.add(emptyBlockIndex + 1, Block(remaining, -1))
            }
            mergeEmptyBlocks(blockList)
            // println(blockList)
        }
    }

    private fun mergeEmptyBlocks(blocks: MutableList<Block>) {
        var (i, max) = 0 to blocks.size - 1
        do {
            if (blocks[i].isFree() && blocks[i + 1].isFree()) {
                blocks[i].size += blocks[i + 1].size
                blocks.removeAt(i + 1)
                //println("Merged empty $i with next")
                max--
            } else {
                i++
            }
        } while (i < max)
    }

    private fun calculate(info: FsInfo): Long {
        val resultat = info.blockList.foldIndexed(0 to 0L) { index, (realIndex, acc: Long), block ->
            val newRealIndex = realIndex + block.size
            if (block.fileId == -1) {
                Pair(newRealIndex, acc)
            } else {
                var accNew = acc
                (realIndex..newRealIndex - 1).forEach { currentIndex ->
                    val extra = currentIndex * block.fileId
                    accNew += extra
                    accNew
                }
                Pair(newRealIndex, accNew)
            }
        }
        return resultat.second
    }

//    private fun printFs(fileSystemBlocks: Array<Int>) {
//        val fs = fileSystemBlocks
//            .map { when (it) { -1 -> '.' else -> it } }
//            .joinToString("")
//        println(fs)
//    }
}

