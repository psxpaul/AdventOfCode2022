class DayEighteeen: AdventDay() {
    override fun partOne(data: List<String>) {
        val grid = parse(data)
        val result = grid.map { p ->
            var sides = 6
            if (grid.contains(Point(p.x - 1, p.y, p.z))) sides--
            if (grid.contains(Point(p.x + 1, p.y, p.z))) sides--
            if (grid.contains(Point(p.x, p.y - 1, p.z))) sides--
            if (grid.contains(Point(p.x, p.y + 1, p.z))) sides--
            if (grid.contains(Point(p.x, p.y, p.z - 1))) sides--
            if (grid.contains(Point(p.x, p.y, p.z + 1))) sides--
            sides
        }
        println("Part 1 - ${result.sum()}")
    }

    override fun partTwo(data: List<String>) {
        val grid = parse(data).associateWith {
            1
        }.toMutableMap()
        fill(grid)
        val result = grid.map { (p, v) ->
            if (v == 0) return@map 0
            var sides = 0
            if (grid[Point(p.x - 1, p.y, p.z)] == 0) sides++
            if (grid[Point(p.x + 1, p.y, p.z)] == 0) sides++
            if (grid[Point(p.x, p.y - 1, p.z)] == 0) sides++
            if (grid[Point(p.x, p.y + 1, p.z)] == 0) sides++
            if (grid[Point(p.x, p.y, p.z - 1)] == 0) sides++
            if (grid[Point(p.x, p.y, p.z + 1)] == 0) sides++
            sides
        }

        println("Part 2 - ${result.sum()}")
    }

    private fun fill(grid: MutableMap<Point, Int>) {
        val queue = mutableListOf(Point(0, 0, 0))
        val minX = grid.keys.minOfOrNull { it.x }!! - 1
        val maxX = grid.keys.maxOfOrNull { it.x }!! + 1
        val minY = grid.keys.minOfOrNull { it.y }!! - 1
        val maxY = grid.keys.maxOfOrNull { it.y }!! + 1
        val minZ = grid.keys.minOfOrNull { it.z }!! - 1
        val maxZ = grid.keys.maxOfOrNull { it.z }!! + 1

        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()

            if (grid.contains(p) ||
                p.x < minX || p.x > maxX ||
                p.y < minY || p.y > maxY ||
                p.z < minZ || p.z > maxZ) continue

            grid[p] = 0
            queue.add(Point(p.x + 1, p.y, p.z))
            queue.add(Point(p.x - 1, p.y, p.z))
            queue.add(Point(p.x, p.y + 1, p.z))
            queue.add(Point(p.x, p.y - 1, p.z))
            queue.add(Point(p.x, p.y, p.z + 1))
            queue.add(Point(p.x, p.y, p.z - 1))
        }
    }

    private fun parse(data: List<String>) = data.map { s -> s.split(",").map { it.toInt() }.let { (x, y, z) -> Point(x, y, z) } }.toSet()
    data class Point(var x: Int, var y: Int, var z: Int)

    enum class ThreeDeeState {
        GAS,
        SOLID,
        UNKNOWN,
    }
}