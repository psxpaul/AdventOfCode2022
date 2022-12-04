class DayFour: AdventDay(4) {
    override fun partOne(data: List<String>) {
        val total = data.count {
            val elves = parse(it)
            fullyCovers(elves.first, elves.second) || fullyCovers(elves.second, elves.first)
        }
        println("Part 1 - $total")
    }

    override fun partTwo(data: List<String>) {
        val total = data.count {
            val elves = parse(it)
            val overlap = overlap(elves.first, elves.second) || fullyCovers(elves.first, elves.second) || fullyCovers(elves.second, elves.first)
            overlap
        }
        println("Part 2 - $total")
    }

    private fun parse(line: String) = line.split(',').map { p ->
        p.split('-').let { r -> Pair(r[0].toInt(), r[1].toInt()) }
    }.let { Pair(it[0], it[1]) }

    private fun fullyCovers(elfOne: Pair<Int, Int>, elfTwo: Pair<Int, Int>) =
        elfOne.first <= elfTwo.first && elfOne.second >= elfTwo.second

    private fun overlap(elfOne: Pair<Int, Int>, elfTwo: Pair<Int, Int>) =
        (elfOne.first >= elfTwo.first && elfOne.first <= elfTwo.second) ||
                (elfOne.second >= elfTwo.first && elfOne.second <= elfTwo.second)
}