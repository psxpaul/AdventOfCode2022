class DaySeventeen: AdventDay() {
    companion object {
        private val CAVE_WIDTH = 7
        private val PART_TWO_ROCK_COUNT = 1000000000000
        private val ROCK_SHAPES = listOf(
            setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)), // DASH shape
            setOf(Position(1, 0), Position(0, 1), Position(1, 1), Position(2, 1), Position(1, 2)), // PLUS shape
            setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1), Position(2, 2)), // L shape
            setOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)), // PIPE shape
            setOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1)), // SQUARE shape
        )
    }

    override fun partOne(data: List<String>) {
//        val pattern = parsePattern(data)
//        dropRocks(2022, pattern, true)
    }

    override fun partTwo(data: List<String>) {
        val pattern = parsePattern(data)
        dropRocks(100_000, pattern, true)
//        println("Part 2 - TODO")
    }

    private fun dropRocks(numRocks: Int, pattern: List<Direction>, partTwo: Boolean) {
        val cave = mutableSetOf<Position>()
        val patternIndexesSeen = mutableSetOf<Int>()
        var patternOffsetFound: Int? = null
        var rockNumBeforePatternStarted: Int? = null
        val maxYInPattern = mutableListOf<Int>()
        var maxY = 0
        var patternIdx = 0
        fun nextMove() = pattern[patternIdx % pattern.size].also { patternIdx++ }.let { if (it == Direction.LEFT) -1 else 1 }
        (0 until CAVE_WIDTH).forEach { cave.add(Position(it, 0)) } // add cave floor

        (0 until numRocks).forEach { rockNum ->
            var rock = ROCK_SHAPES[rockNum % ROCK_SHAPES.size]
                .map { pos -> Position(pos.x + 2, pos.y + maxY + 4) }

            while (true) {
                val movedRock = nextMove().let { m -> rock.map { pos -> Position(pos.x + m, pos.y) } }
                rock = if (validRock(movedRock, cave)) movedRock else rock

                val loweredRock = rock.map { pos -> Position(pos.x, pos.y - 1) }
                rock = if (validRock(loweredRock, cave)) loweredRock else break
            }

            cave.addAll(rock)
            maxY = maxOf(maxY, rock.maxBy { it.y }.y)

            if (partTwo && (rockNum != 0) && (rockNum % ROCK_SHAPES.size == 0)) {
                val patternOffset = patternIdx % pattern.size
                if (patternOffsetFound == null) {
                    if (!patternIndexesSeen.add(patternOffset)) {
                        println("Pattern found at $rockNum")
                        patternOffsetFound = patternOffset
                        rockNumBeforePatternStarted = rockNum
                    }
                } else if (patternOffset == patternOffsetFound) {
                    val cycleRockCount = rockNum - rockNumBeforePatternStarted!!
                    val cycleHeight = maxY - maxYInPattern[0]
                    val rocksRemaining = PART_TWO_ROCK_COUNT - rockNum
                    val cyclesRemaining = rocksRemaining / cycleRockCount
                    val rocksAfterLastCycle = (rocksRemaining % cycleRockCount).toInt()
                    val heightAfterLastCycle = maxYInPattern[rocksAfterLastCycle] - maxYInPattern[0]

                    val endHeight = (cycleHeight * cyclesRemaining) + maxY - 1  + heightAfterLastCycle
                    println(endHeight)
                    return
                }
            }
            if (patternOffsetFound != null) {
                maxYInPattern.add(maxY)
            }
        }

        throw IllegalArgumentException("no cycle found")
    }

    private fun validRock(rock: List<Position>, cave: Set<Position>): Boolean {
        val rockPoints = rock.toSet()
        if (cave.intersect(rockPoints).isNotEmpty())
            return false
        if (rock.any { it.x < 0 || it.x >= CAVE_WIDTH })
            return false
        return true
    }

    private fun print(rocks: Set<Position>, maxY: Int, candidate: List<Position>) {
        val top = maxOf(maxY, candidate.maxOfOrNull { it.y } ?: 0)
        (0 .. top).reversed().forEach { j ->
            if (j == 0) print('+') else print('|')
            (0 until CAVE_WIDTH).forEach { i ->
                val c = if (j == 0) '-' else '#'
                if (rocks.contains(Position(i, j))) print(c)
                else if (candidate.contains(Position(i, j))) print('@')
                else print('.')
            }
            if (j == 0) println('+') else println('|')
        }
        println("\n\n")
    }

    private fun parsePattern(data: List<String>) = data[0].toCharArray().map { Direction.from(it) }

    enum class Direction {
        LEFT, RIGHT;

        companion object {
            fun from(c: Char) = when(c) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> throw IllegalArgumentException("Bad direction $c")
            }
        }
    }
}