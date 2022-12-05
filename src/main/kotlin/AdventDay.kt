import java.nio.file.Files
import java.nio.file.Path

sealed class AdventDay(private val dayNumber: Int) {
    abstract fun partOne(data: List<String>)
    abstract fun partTwo(data: List<String>)

    fun run(dryRun: Boolean) {
        partOne(data(dryRun))
        partTwo(data(dryRun))
    }

    private fun data(dryRun: Boolean) = Files.readAllLines(Path.of(
        (javaClass.getResource("day$dayNumber${if (dryRun) "_test" else ""}.dat") ?: throw RuntimeException("Could not find data file")).toURI()
    ))
}

fun main() {
    DayFive().run(false)
}