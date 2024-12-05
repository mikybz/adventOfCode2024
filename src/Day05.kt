/*
75|13
53|13

75,47,61,53,29
97,61,53,29,13
 */
fun main(args: Array<String>) = dayRunner(Day05())

class Day05 : DayAdvent {

    override fun part1(input: List<String>): Any? { //5948
        val (rules, pages) = parseInput(input)
        return pages
            .mapNotNull { getMiddlePageIfCorrect(it, rules)}
            .trackDebugState()
            .sum()
    }

    private fun parseInput(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
        var (inputRules, inputPages) = input.subListSplitter { it.isEmpty() }
        val rules: List<List<Int>> = inputRules.map { it.split("|").map { nr -> nr.toInt() } }
        val pages: List<List<Int>> = inputPages.map { it.split(",").map { nr -> nr.toInt() } }
        return Pair(rules, pages)
    }

    private fun getMiddlePageIfCorrect(pages: List<Int>, rules: List<List<Int>>): Int? =
        (pages[pages.size / 2]).takeIf {
            pages.indices.all { isPageNrCorrect(it, pages, rules) }
        }

    private fun getMiddlePageIfNotCorrect(pages: List<Int>, rules: List<List<Int>>): Int? {
        if (getMiddlePageIfCorrect(pages, rules) != null) {
            return null
        }
        val pageFixIterations = mutableListOf<List<Int>>(pages)
        var counter = 0
        while (getMiddlePageIfCorrect(pageFixIterations.last(), rules) == null) {
            if(counter++>1000) throw Exception("Infinite loop")
            var pagesToFix = pageFixIterations.last().trackDebugState()
            val firstFailIndex = pagesToFix.indices.first { index -> !isPageNrCorrect(index, pagesToFix, rules) }
            pageFixIterations.add(swapIndexWitherror(firstFailIndex, pagesToFix, rules))
        }
        var pagesFixed = pageFixIterations.last()

        return (pagesFixed[pagesFixed.size / 2])

    }

private fun isPageNrCorrect(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): Boolean {
    val pageNr = pages[pageIndex]
    val firstPageIndexWithPage = pages.indices.first { pages[it] == pageNr }
//        val pagesBefore = pages.subList(0, firstPageIndexWithPage)
    val pagesAfter = pages.subList(firstPageIndexWithPage + 1, pages.size)
    val pagesMustBeAfter = rules.filter { it[0] == pageNr }.map { it[1] }.filter { pages.contains(it) }
    val ruleResult = pagesMustBeAfter.isEmpty() || pagesMustBeAfter.all { it in pagesAfter }

    return ruleResult
}

private fun swapIndexWitherror(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): List<Int> {
    val pageNr = pages[pageIndex]
    val firstPageIndexWithPage = pages.indices.first { pages[it] == pageNr }
    val pagesAfter = pages.subList(firstPageIndexWithPage + 1, pages.size)
    val pagesMustBeAfter = rules.filter { it[0] == pageNr }.map { it[1] }.filter { pages.contains(it) }
    val missingPagesAfter = pagesMustBeAfter.filter { it !in pagesAfter }
    val indexesOfMissingAfterPages = missingPagesAfter.map { pages.indexOf(it) }
    val lastIndexesOfMissingAfterPages = indexesOfMissingAfterPages.maxOf { it }

    return pages.swapElements(firstPageIndexWithPage, lastIndexesOfMissingAfterPages)
}


override fun part2(input: List<String>): Any? { //5948
    val (rules, pages) = parseInput(input)
    val sum = pages
        .trackDebugState(reset = true)
        .mapNotNull() {
            //println(it)
            getMiddlePageIfNotCorrect(it, rules)
        }
        .trackDebugState()
        .sum()
//        val sum = 3
//        val middlePageIfCorrect = getMiddlePageIfCorrect(pages[3], rules)

    return sum
}
}

