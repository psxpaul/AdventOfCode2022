import java.nio.file.Files
import java.nio.file.Path

// val lines = listOf(
//     "vJrwpWtwJgWrhcsFMMfFFhFp",
//     "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
//     "PmmdzqPrVvPwwTWBwg",
//     "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
//     "ttgJtRGJQctTZtZT",
//     "CrZsJsPPZsGzwwsLwLmpwMDw",
// )

val dataUrl = javaClass.getResource("day3.dat") ?: throw RuntimeException("Could not find data file")
val lines = Files.readAllLines(Path.of(dataUrl.toURI()))

var prioritySum = 0
/* part 1
lines.forEach {
    val compartments = it.chunked(it.length / 2)
    val commonChar = commonCharTwo(compartments)
    val priority = priority(commonChar)
    prioritySum += priority

    println("$compartments     $commonChar    $priority")
}
 */

/* part 2 */
val groups = lines.chunked(3)
groups.withIndex().forEach {
    val commonChar = commonCherThree(it.value)
    val priority = priority(commonChar)
    prioritySum += priority
    println("${it.index} - $commonChar  $priority")
}
println("$prioritySum")

fun commonCharTwo(strings: List<String>): Char {
    val s2Chars = strings[1].toSet()
    return strings[0].find { s2Chars.contains(it) }!!
}

fun commonCherThree(strings: List<String>): Char {
    val s2Chars = strings[1].toSet()
    val s3Chars = strings[2].toSet()
    return strings[0].find { s2Chars.contains(it) && s3Chars.contains(it) }!!
}

fun priority(char: Char) =
    if (char.isUpperCase())
        char.code - 'A'.code + 27
    else
        char.code - 'a'.code + 1