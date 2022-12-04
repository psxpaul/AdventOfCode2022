class DayOne: AdventDay(1) {
    override fun partOne(data: List<String>) {
        println("Richest elf has ${results(data)[0].totalCalories()} calories")
    }

    override fun partTwo(data: List<String>) {
        println("Three richest elves have ${results(data).map { it.totalCalories() }.sum()} calories")
    }

    private fun results(lines: List<String>): List<Elf> {
        val elves = mutableListOf<Elf>()
        var snacks = mutableListOf<Snack>()
        lines.forEach { calories ->
            if (calories.isBlank()) {
                elves.add(Elf(snacks))
                snacks = mutableListOf()
            } else {
                snacks.add(Snack(calories.toInt()))
            }
        }
        elves.add(Elf(snacks))
        elves.sort()
        return elves.take(3)
    }
}

data class Elf(
    val snacks: List<Snack>,
): Comparable<Elf> {
    fun totalCalories() = snacks.map { it.calories }.sum()
    override fun compareTo(other: Elf) = other.totalCalories().compareTo(totalCalories())
}

data class Snack(
    val calories: Int
)