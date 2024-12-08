fun main() = dayRunner(Day05())

class Day05 : DayAdvent {

    override fun part1(input: List<String>): Any? { //5948
        val (rules, pages) = parseInput(input)
        return pages
            .mapNotNull { getMiddlePageIfCorrect(it, rules) }
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
            pages.indices.all { doesCurrentIndexComplyToAllRules(it, pages, rules) }
        }

    private fun doesCurrentIndexComplyToAllRules(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): Boolean =
        pagesRequiredToBeAfterIndexButIsBefore(pageIndex, pages, rules).isEmpty()

    private fun pagesRequiredToBeAfterIndexButIsBefore(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): List<Int> {
        val pageNr = pages[pageIndex]
        val pagesMustBeAfter = rules
            .filter { it[0] == pageNr }
            .map { it[1] }
            .filter { pages.contains(it) }

        val firstPageIndexWithPageNr = pages.indices.first { pages[it] == pageNr }
        val pagesAfter = pages.subList(firstPageIndexWithPageNr + 1, pages.size)
        return pagesMustBeAfter.filter { it !in pagesAfter }
    }


    /*******************************
     ***        Part 2           ***
     *******************************/
    override fun part2(input: List<String>): Any? { //5948
        val (rules, pages) = parseInput(input)
        return pages
            .mapNotNull { getMiddlePageIfNotCorrect(it, rules) }
            .sum()
    }

    private fun isOrderCorrect(pages: List<Int>, rules: List<List<Int>>): Boolean =
        getMiddlePageIfCorrect(pages, rules) != null

    private fun getMiddlePageIfNotCorrect(pages: List<Int>, rules: List<List<Int>>): Int? {
        if (isOrderCorrect(pages, rules)) return null

        var pagesIterator = pages
        // Each time we detect error, we swap the first index violating the rules, then try again
        while (!isOrderCorrect(pagesIterator, rules)) {
            val firstFailIndex = pagesIterator.indices
                .first { index -> !doesCurrentIndexComplyToAllRules(index, pagesIterator, rules) }
            pagesIterator = swapIndexWitherror(firstFailIndex, pagesIterator, rules)
        }
        return (pagesIterator[pagesIterator.size / 2])
    }

    private fun swapIndexWitherror(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): List<Int> {
        val lastIndexesOfMissingAfterPages =
            pagesRequiredToBeAfterIndexButIsBefore(pageIndex, pages, rules)
                .map { pages.indexOf(it) }
                .maxOf { it }
        return pages.swapElements(pageIndex, lastIndexesOfMissingAfterPages)
    }
}
