/*
75|13
53|13

75,47,61,53,29
97,61,53,29,13
 */
fun main(args: Array<String>) = dayRunner(Day05())

class Day05 : DayAdvent {

    override fun part1(input: List<String>): Any? { //5948
        var (inputRules, inputPages) = input.subListSplitter { it.isEmpty() }
        val rules = inputRules.map { it.split("|").map { nr -> nr.toInt() } }
        val pages = inputPages.map { it.split(",").map { nr -> nr.toInt() } }
        val sum = pages.mapNotNull() {
            println(it)
            getMiddlePageIfCorrect(it, rules)
        }
            .trackDebugState()
            .sum()
//        val sum = 3
//        val middlePageIfCorrect = getMiddlePageIfCorrect(pages[3], rules)

        return sum
    }

    private fun getMiddlePageIfCorrect(pages: List<Int>, rules: List<List<Int>>): Int? =
        (pages[pages.size / 2]).takeIf {
            pages.indices.all { isPageNrCorrect(it, pages, rules) }
        }

    private fun getMiddlePageIfNotCorrect(pages: List<Int>, rules: List<List<Int>>): Int? {
        if(getMiddlePageIfCorrect(pages, rules) != null) {
            return null
        }
        val pageFixIterations = mutableListOf<List<Int>>(pages)
        while( getMiddlePageIfCorrect(pageFixIterations.last(), rules)==null) {

        }

        return (pages[pages.size / 2]).takeIf {
            pages.indices.all { isPageNrCorrect(it, pages, rules) }
        }
    }

    private fun isPageNrCorrect(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): Boolean {
        val pageNr = pages[pageIndex]
        val firstPageIndexWithPage = pages.indices.first { pages[it] == pageNr }
//        val pagesBefore = pages.subList(0, firstPageIndexWithPage)
        val pagesAfter = pages.subList(firstPageIndexWithPage + 1, pages.size)
        val pagesMustBeAfter = rules.filter { it[0] == pageNr }.map { it[1] }.filter { pages.contains(it) }
        val ruleResult = pagesMustBeAfter.isEmpty() || pagesMustBeAfter.all() { it in pagesAfter }

        return ruleResult
    }

    private fun indexWitherror(pageIndex: Int, pages: List<Int>, rules: List<List<Int>>): Boolean {
        val pageNr = pages[pageIndex]
        val firstPageIndexWithPage = pages.indices.first { pages[it] == pageNr }
//        val pagesBefore = pages.subList(0, firstPageIndexWithPage)
        val pagesAfter = pages.subList(firstPageIndexWithPage + 1, pages.size)
        val pagesMustBeAfter = rules.filter { it[0] == pageNr }.map { it[1] }.filter { pages.contains(it) }

        val missingPages = pagesMustBeAfter.filter { it !in pagesAfter }
        return ruleResult
    }


    override fun part2(input: List<String>): Any? { //5948
        var (inputRules, inputPages) = input.subListSplitter { it.isEmpty() }
        val rules = inputRules.map { it.split("|").map { nr -> nr.toInt() } }
        val pages = inputPages.map { it.split(",").map { nr -> nr.toInt() } }
        val sum = pages
            .trackDebugState(reset = true)
            .mapNotNull() {
            println(it)
            getMiddlePageIfNotCorrect(it, rules)
        }
            .trackDebugState()
            .sum()
//        val sum = 3
//        val middlePageIfCorrect = getMiddlePageIfCorrect(pages[3], rules)

        return sum
    }
}

