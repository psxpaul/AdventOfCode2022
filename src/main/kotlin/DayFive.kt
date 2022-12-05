import java.lang.RuntimeException

class DayFive: AdventDay() {
    companion object {
        private val STACK_REGEX = Regex("\\[[A-Z]+\\]")
        private val MOVE_REGEX = Regex("^move (\\d+) from (\\d+) to (\\d+)$")
    }
    override fun partOne(data: List<String>) {
        val stacks = mutableListOf<ArrayDeque<String>>()

        data.forEach {
            val stackMatch = STACK_REGEX.findAll(it)
            val moveMatch = MOVE_REGEX.find(it)
            if (stackMatch.any()) {
                stackMatch.forEach {
                    val stackNum = it.range.first / 4
                    val value = it.value.replace(Regex("^\\["), "").replace(Regex("]$"), "")
                    while (stacks.size < (stackNum + 1)) {
                        stacks.add(ArrayDeque())
                    }
                    stacks[stackNum].addLast(value)
                }
            } else if (moveMatch != null) {
                val amount = moveMatch.groups[1]?.value?.toInt()!!
                val source = moveMatch.groups[2]?.value?.toInt()!! - 1
                val dest = moveMatch.groups[3]?.value?.toInt()!! - 1

                (1..amount).forEach {
                    val value = stacks[source].removeFirst()
                    stacks[dest].addFirst(value)
                }
            } else {
                // println("ignoring line - $it")
            }
        }
        println("Part 1 - ${stacks.map { it.first() }.joinToString("")}")
    }

    override fun partTwo(data: List<String>) {
        val stacks = mutableListOf<MutableList<String>>()

        data.forEach {
            val stackMatch = STACK_REGEX.findAll(it)
            val moveMatch = MOVE_REGEX.find(it)
            if (stackMatch.any()) {
                stackMatch.forEach {
                    val stackNum = it.range.first / 4
                    val value = it.value.replace(Regex("^\\["), "").replace(Regex("]$"), "")
                    while (stacks.size < (stackNum + 1)) {
                        stacks.add(ArrayDeque())
                    }
                    stacks[stackNum].add(value)
                }
            } else if (moveMatch != null) {
                val amount = moveMatch.groups[1]?.value?.toInt()!!
                val source = moveMatch.groups[2]?.value?.toInt()!! - 1
                val dest = moveMatch.groups[3]?.value?.toInt()!! - 1

                stacks[dest].addAll(0, stacks[source].take(amount))
                (1..amount).forEach { stacks[source].removeFirst() }
            } else {
                // println("ignoring line - $it")
            }
        }
        println("Part 2 - ${stacks.map { it.first() }.joinToString("")}")
    }
}