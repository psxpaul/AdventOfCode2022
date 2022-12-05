import java.nio.file.Files
import java.nio.file.Path

sealed class AdventDay {
    abstract fun partOne(data: List<String>)
    abstract fun partTwo(data: List<String>)

    fun run(dryRun: Boolean) {
        partOne(data(dryRun))
        partTwo(data(dryRun))
    }

    private fun data(dryRun: Boolean) = Files.readAllLines(Path.of(
        (javaClass.getResource("${javaClass.simpleName}/${if (dryRun) "test" else "input"}.dat") ?: throw RuntimeException("Could not find data file")).toURI()
    ))
}

fun main() {
    DaySix().run(true)
}