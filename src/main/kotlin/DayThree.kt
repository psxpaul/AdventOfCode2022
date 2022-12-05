class DayThree: AdventDay() {
    override fun partOne(data: List<String>) {
        var prioritySum = 0
        data.forEach {
            val compartments = it.chunked(it.length / 2)
            val commonChar = commonCharTwo(compartments)
            val priority = priority(commonChar)
            prioritySum += priority

            // println("$compartments     $commonChar    $priority")
        }
        println("Part1 sum - $prioritySum")
    }

    override fun partTwo(data: List<String>) {
        var prioritySum = 0
        val groups = data.chunked(3)
        groups.withIndex().forEach {
            val commonChar = commonCherThree(it.value)
            val priority = priority(commonChar)
            prioritySum += priority
            // println("${it.index} - $commonChar  $priority")
        }
        println("Part2 sum - $prioritySum")
    }

    private fun commonCharTwo(strings: List<String>): Char {
        val s2Chars = strings[1].toSet()
        return strings[0].find { s2Chars.contains(it) }!!
    }

    private fun commonCherThree(strings: List<String>): Char {
        val s2Chars = strings[1].toSet()
        val s3Chars = strings[2].toSet()
        return strings[0].find { s2Chars.contains(it) && s3Chars.contains(it) }!!
    }

    private fun priority(char: Char) =
        if (char.isUpperCase())
            char.code - 'A'.code + 27
        else
            char.code - 'a'.code + 1
}