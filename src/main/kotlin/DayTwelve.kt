class DayTwelve: AdventDay() {
    override fun partOne(data: List<String>) {
        val mapState = parse(data)
        val result = mapState.shortestPath()
        println("Part 1 - ${result}")
    }

    override fun partTwo(data: List<String>) {
        val mapState = parse(data)
        val bottoms = mutableListOf<Position>()
        mapState.grid.withIndex().forEach { (j, row) ->
           row.withIndex().forEach { (i, height)  ->
               if (height == 0) bottoms.add(Position(i, j))
           }
        }

        val result = bottoms.mapNotNull {
            mapState.shortestPath(it)
        }.min()
        println("Part 2 - $result")
    }

    fun parse(data: List<String>): MapState {
        var start: Position? = null
        var end: Position? = null
        val grid = data.withIndex().map { (j, row) ->
            row.toCharArray().withIndex().map { (i, c) ->
                when (c) {
                    'S' -> {
                        start = Position(i, j)
                        0
                    }
                    'E' -> {
                        end = Position(i, j)
                        25
                    }
                    else -> c - 'a'
                }
            }
        }
        return MapState(grid, start!!, end!!)
    }
}

data class MapState(
    val grid: List<List<Int>>,
    val starting: Position,
    val ending: Position,
) {

    fun shortestPath(from: Position = starting): Int? {
        val shortestToPos = mutableMapOf(from to 0)
        val previousStepToPos = mutableMapOf<Position, Position>()
        val queue = mutableListOf(from)
        while (queue.size > 0) {
            var current = queue.removeFirst()
            if (current == ending) {
                val path = mutableListOf<Position>()
                while (previousStepToPos.contains(current)) {
                    path.add(current)
                    current = previousStepToPos[current]!!
                }
                return path.size
            }

            val currentHeight = grid[current.y][current.x]
            listOf(
                current.copy(x = current.x - 1), // left
                current.copy(x = current.x + 1), // right
                current.copy(y = current.y - 1), // down
                current.copy(y = current.y + 1), // up
            ).filter {
                it.x >= 0 && it.x < grid[0].size &&
                        it.y >= 0 && it.y < grid.size &&
                        grid[it.y][it.x] <= currentHeight + 1
            }.forEach {
                val distToPos = shortestToPos[current]!! + 1
                if (distToPos < (shortestToPos[it] ?: Int.MAX_VALUE)) {
                    shortestToPos[it] = distToPos
                    previousStepToPos[it] = current
                    queue.add(it)
                }
            }
        }

        return null
    }
}