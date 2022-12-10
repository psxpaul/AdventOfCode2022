class DayTen: AdventDay() {
    override fun partOne(data: List<String>) {
        val cycles = parse(data)

        /*cycles.withIndex().forEach {
            println("cycle ${it.index+1} : ${it.value.signalStrength(it.index+1)}     ${it.value.startValue}      ${it.value.endValue}")
        }*/

        val result = listOf(20, 60, 100, 140, 180, 220).map { cycles[it-1].signalStrength(it) }
        println("Part 1 - ${result}   ${result.sum()}")
    }

    override fun partTwo(data: List<String>) {
        println("Part 2 - ")
        val cycles = parse(data)
        var i = 0
        (1..6).forEach {
            (1..40).forEach { x ->
                val spriteStart = cycles[i++].startValue
                val spriteEnd = spriteStart + 2
                if (x in spriteStart..spriteEnd) print("#")
                else print(".")
            }
            print("\n")
        }

    }

    private fun parse(data: List<String>): MutableList<CycleState> {
        val cycles = mutableListOf<CycleState>()
        data.forEach {
            val startValue = if (cycles.isEmpty()) 1 else cycles.last().endValue
            if (it == "noop") {
                cycles.add(CycleState(Command.noop, 0, startValue, startValue))
            } else {
                val (_, amount) = it.split(" ")
                cycles.add(CycleState(Command.addx, amount.toInt(), startValue, startValue))
                cycles.add(CycleState(Command.addx, amount.toInt(), startValue, startValue + amount.toInt()))
            }
        }
        return cycles
    }
}

data class CycleState(
    val command: Command,
    val amount: Int,
    var startValue: Int,
    var endValue: Int,
) {
    fun signalStrength(cycleNum: Int) = cycleNum * startValue!!
}

enum class Command {
    noop,
    addx,
}
