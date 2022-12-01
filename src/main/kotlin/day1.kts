import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path

val dataUrl = javaClass.getResource("day1.dat") ?: throw RuntimeException("Could not find data file")
val lines = Files.readAllLines(Path.of(dataUrl.toURI()))

println("Lines: $lines")

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
println("1st Richest elf: ${elves[0].totalCalories()}")
println("2nd Richest elf: ${elves[1].totalCalories()}")
println("3rd Richest elf: ${elves[2].totalCalories()}")
println("Top 3 richest elves: ${elves.take(3).map { it.totalCalories() }.sum()}")

data class Elf(
    val snacks: List<Snack>,
): Comparable<Elf> {
    fun totalCalories() = snacks.map { it.calories }.sum()
    override fun compareTo(other: Elf) = other.totalCalories().compareTo(totalCalories())
}

data class Snack(
    val calories: Int
)