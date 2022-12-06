class DaySix: AdventDay() {
    override fun partOne(data: List<String>) {
        val result = data.map { s ->
            s.windowed(4, 1, false).withIndex().first { iv ->
                iv.value.toCharArray().toSet().size == 4
            }.index + 4
        }
        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val result = data.map { s ->
            s.windowed(14, 1, false).withIndex().first { iv ->
                iv.value.toCharArray().toSet().size == 14
            }.index + 14
        }
        println("Part 2 - $result")
    }
}