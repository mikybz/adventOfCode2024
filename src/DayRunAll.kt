class DayRunAll() {
    companion object {
        val allDays: Array<DayAdvent> =
            arrayOf(Day01(), Day02(), Day03(), Day04(), Day05(), Day06(), Day07(), Day08())

        @JvmStatic
        fun main(args: Array<String>) {
            println("Running all days")
            globalTracing = false // For speedup
            val readStoredResults = readAdventResults().associateBy { it.day }

            for (day in allDays) {
                val dayName = day.javaClass.simpleName
                // Running current day - prints result internally
                val result = dayRunWithResult(day)

                // Compare with stored results
                val expected: AdventResults? = readStoredResults[dayName]
                if (expected == null) {
                    println("Add this to Solutions.txt:")
                    println("$result")
                } else if (expected != result) {
                    println("Day $dayName failed. Expected:")
                    println("x $expected")
                    println()
                }
            }
            println("Done")
        }
    }
}
