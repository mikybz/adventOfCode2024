fun main(args: Array<String>) = dayRunner(Day07())

class Day07 : DayAdvent {

    override fun part1(input: List<String>): Any {
        return 0
//        val equations = input.parse()
//        val validEquations = equations.map { (lineresult, testValues) ->
//            Triple(lineresult, testValues, validEquationsPlusMul(lineresult, testValues))
//        } .filter{ it.third.isNotEmpty() }
//
////        validEquations.forEach{ (lineresult, testValues, validEquations)->
////            println("lineresult=$lineresult testValues=$testValues validEquations=$validEquations")
////        }
//        val sum = validEquations.sumOf { it.first }
////        println("sum=$sum")
//
//        return sum
    }

    override fun part2(input: List<String>): Any {
        val equations = input.parse()
        val validEquations = equations.filter { (lineresult, testValues) ->
            validEquationsPlusMulConc(lineresult, testValues)
        }

//        validEquations.forEach{ (lineresult, testValues, validEquations)->
//            println("lineresult=$lineresult testValues=$testValues validEquations=$validEquations")
//        }
        val sum = validEquations.sumOf { it.first }
        //println("sum=$sum")

        return sum
    }

    private fun validEquationsPlusMul(results: Long, testValues: List<Int>): List<String> {
        val allOperators = allOperatorsPlusMul(testValues)
        val validOperators = ArrayList<List<Char>>()
        val validEquations = ArrayList<String>()

        allOperators.map { operators ->

//println("testValues=$testValues  operators=$operators")

            val firstValue = testValues.first().toLong()

            val result = testValues.drop(1).zip(operators).fold(firstValue) { acc, (value, operator) ->
//                println("acc=$acc value=$value operator=$operator")
                when (operator) {
                    '+' -> acc + value
                    '*' -> acc * value
                    else -> throw IllegalArgumentException("Unknown operator $operator")
                }
            }
            if (result == results) {
//                println("result=$result")
                validOperators.add(operators)
                validEquations.add(mergeAlternating(testValues, operators).joinToString(" "))
            }
//            else {
//                println("resultxx=$result")
//            }
        }

        return validEquations
    }


    private fun allOperatorsPlusMul(ints: List<Int>): List<List<Char>> {
//        print("ints=$ints ")
        val operators = ints.size - 1
        if (operators < 1) return listOf()

        val opNumber = (1 shl (operators)) - 1
        val numbers = (0..opNumber).toList()
        val allOperands = numbers
            .map { op: Int ->
                //println(op)
                op.toString(2)
                    .padStart(operators, '0')
                    .map { if (it == '1') '+' else '*' }

            }

        return allOperands.trackDebugState()
    }

    fun recursiveOpsBuilder(
        listOps: List<List<Char>>,
        index: Int,
        endIndex: Int,
        operators3: List<Char>
    ): List<List<Char>> {
        if (index == endIndex) return listOps

        val newListOps: List<List<Char>> = listOps.flatMap { op ->
            operators3.map { op + it } // Concatenate 'op' with 'it'
        }

        return recursiveOpsBuilder(newListOps, index + 1, endIndex, operators3)
    }

    fun iterativeOpsBuilder(opCount: Int,operators: List<Char>): List<List<Char>> {
        if (opCount < 1) return emptyList()

        // Start with single-character operator lists
        var currentOps = operators.map { listOf(it) }

        // Iteratively build combinations
        repeat(opCount - 1) {
            currentOps = currentOps.flatMap { op ->
                operators.map { op + it } // Concatenate existing ops with new operators
            }
        }

        return currentOps
    }

    private fun allOperatorsPlusMulConc(ints: List<Int>): List<List<Char>> {
        val opCount = ints.size - 1
        if (opCount < 1) return emptyList()
        val operators = listOf('+', '*', '|')

        var operatorBuildIterator = operators.map { listOf(it) }
        repeat(opCount - 1) {
            // For hvert eksisterende liste-element lager vi 3 Nye sublister med hve sin end-operator
            operatorBuildIterator = operatorBuildIterator.flatMap { op ->
                operators.map { op + it }
            }
        }

        return operatorBuildIterator
    }


    private fun validEquationsPlusMulConc(results: Long, testValues: List<Int>): Boolean{
        val allOperators = allOperatorsPlusMulConc(testValues)
//        val validOperators = ArrayList<List<Char>>()
//        val validEquations = ArrayList<String>()

        allOperators.map { operators ->

//            println("testValues=$testValues  operators=$operators")

            val firstValue = testValues.first().toLong()

            val result = testValues.drop(1).zip(operators).fold(firstValue) { acc, (value, operator) ->
//                println("acc=$acc value=$value operator=$operator")
                when (operator) {
                    '+' -> acc + value
                    '*' -> acc * value
                    '|'-> (acc.toString()+value.toString()).toLong()
                    else -> throw IllegalArgumentException("Unknown operator $operator")
                }
            }
            if (result == results) {
                //println("result=$result")
//                validOperators.add(operators)
//                validEquations.add(mergeAlternating(testValues, operators).joinToString(" "))
                return true
            } else {
//                println("resultxx=$result")
            }
        }

        return false
    }


    fun List<String>.parse(): List<Pair<Long, List<Int>>> =
        map { it.split(": ", limit = 2) }
            .trackDebugState()
            .map { (a, b) ->
                //println("a=$a b=$b")
                a.toString().toLong() to b.toString().split(" ").map { it.toInt() }
            }
}

fun <T> mergeAlternating(listA: List<T>, listB: List<T>): List<T> {
    val minSize = minOf(listA.size, listB.size)
    val interleaved = (0 until minSize).flatMap { listOf(listA[it], listB[it]) }
    return interleaved + listA.drop(minSize) + listB.drop(minSize)
}