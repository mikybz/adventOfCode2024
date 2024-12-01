class CodeExamples  {
    val list1 = mutableListOf("hei")
        .also { it.add(" verden") }
        .apply { add("!!") }

    fun f1() {
        val nr1 = list1.run { size }
        val nr2 = list1.let { it.size }
        var nr5 = 0
        with(list1) {
            add("$nr1")
            nr5=size
        }
        if("$nr1"!=list1.removeLastOrNull()) println("Feil1")
        if((nr5-1)!= list1.size) println("Feil2")

        repeat(2){i: Int -> list1.add(" $i ") }

        val nr10 : Int? = nr1.takeIf { it==nr2 }
        val nr11 : Int? = nr1.takeUnless { it>1000 }

        // Fastest duplicate and add = listOf + flatten:
        val list2 = listOf(list1, listOf("Extended kopi")).flatten()
        //val list3 = buildList { addAll(list1); add("Extended kopi2") }

        val sortedHackList = sortedSetOf<SortObject>()
        sortedHackList.add(SortObject(1))
        val first = sortedHackList.pollFirst()
        println("Ok")


    }

    data class SortObject(val time: Int) : Comparable<SortObject> {
        override fun compareTo(other: SortObject): Int {
            if(this === other) return 0
            if(time == other.time) return 1
            return time - other.time
        }
    }



    companion object {
        @JvmStatic
        fun main(args: Array<String>) = CodeExamples().f1()
    }
}