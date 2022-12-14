class DayFourteen: AdventDay() {
    // expected total sand dropped == 24
    override fun partOne(data: List<String>) {
        val cave = parse(data)
        var result = 0
        while (addSand(cave)) result++
//        printCave(cave)
        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val cave = parse(data)
        val maxY = cave.withIndex().last { it.value.any { c -> c != CavePoint.EMPTY } }.index
        (0 until MAX_CAVE_SIZE).forEach { i ->
            cave[maxY + 2][i] = CavePoint.ROCK
        }
        var result = 0
        while (addSand(cave)) result++
//        printCave(cave)
        println("Part 2 - $result")
    }
}

const val MAX_CAVE_SIZE = 1000
val SAND_SPOUT = Position(500, 0)

fun parse(data: List<String>): MutableList<MutableList<CavePoint>> {
    val grid = data.map { row ->
        row.split(" -> ").map {  pos ->
            pos.split(",").let { c -> Position(c[0].toInt(), c[1].toInt()) }
        }
    }
    val cave = mutableListOf<MutableList<CavePoint>>()
    (0 until MAX_CAVE_SIZE).forEach {
        cave.add((0 until MAX_CAVE_SIZE).map {
            CavePoint.EMPTY
        }.toMutableList())
    }

    grid.forEach { row ->
        row.windowed(2, 1, false).forEach { (pos1, pos2) ->
            (minOf(pos1.x, pos2.x) .. maxOf(pos1.x, pos2.x)).forEach { x ->
                (minOf(pos1.y, pos2.y) .. maxOf(pos1.y, pos2.y)).forEach { y ->
                    cave[y][x] = CavePoint.ROCK
                }
            }
        }
    }
    cave[SAND_SPOUT.y][SAND_SPOUT.x] = CavePoint.SPOUT
    return cave
}

fun addSand(cave: MutableList<MutableList<CavePoint>>): Boolean {
    val pos = SAND_SPOUT.copy()
    if (cave[pos.y][pos.x] == CavePoint.SAND) return false

    while (pos.x in 0 until MAX_CAVE_SIZE-1 && pos.y in 0 until MAX_CAVE_SIZE-1) {
        if (cave[pos.y + 1][pos.x] == CavePoint.EMPTY) {
            pos.y ++
        } else if (cave[pos.y + 1][pos.x - 1] == CavePoint.EMPTY) {
            pos.y ++
            pos.x -= 1
        } else if (cave[pos.y + 1][pos.x + 1] == CavePoint.EMPTY) {
            pos.y ++
            pos.x += 1
        } else {
            cave[pos.y][pos.x] = CavePoint.SAND
            return true
        }
    }

    return false
}

enum class CavePoint(val str: String) {
    EMPTY("."),
    ROCK("#"),
    SAND("0"),
    SPOUT("+"),
}

fun printCave(cave: List<List<CavePoint>>) {
    (0..13).forEach { j ->
        (480 .. 525).forEach { i -> print(cave[j][i].str) }
        println("")
    }
}
