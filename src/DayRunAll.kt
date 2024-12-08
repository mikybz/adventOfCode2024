import kotlin.system.measureTimeMillis

var globalMultiThreadEnabled = true

class DayRunAll() {
    companion object {
        val allDays: Array<DayAdvent> =
            arrayOf(Day01(), Day02(), Day03(), Day04(), Day05(), Day06(), Day07(), Day08())

        @JvmStatic
        fun main(args: Array<String>) {

            globalTracing = false
            globalDebugPrint = false
            println("All args: ${args.joinToString()}")
            if(args.contains("mt")) {
                globalMultiThreadEnabled = true
            }
            if(args.contains("st")) {
                globalMultiThreadEnabled = false
            }


            println("Settings: multiThreadEnabled=$globalMultiThreadEnabled")
            println("Running all days")
            val readStoredResults = readAdventResults().associateBy { it.day }
            val measureTimeMillis = measureTimeMillis {
                runAllTests(readStoredResults)
            }
            println("Total time: $measureTimeMillis ms")
        }

        private fun runAllTests(readStoredResults: Map<String, AdventResults>) {
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
