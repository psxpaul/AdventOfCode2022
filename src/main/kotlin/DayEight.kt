class DayEight: AdventDay() {
    override fun partOne(data: List<String>) {
        val trees = data.map { it.toCharArray().map { c -> c.digitToInt() } }
        val result = (0..trees.size-1).map { x ->
            (0 .. trees.size-1).count { y ->
                isVisible(trees, x, y)
            }
        }.sum()

        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val trees = data.map { it.toCharArray().map { c -> c.digitToInt() } }
        val result = (0 .. trees.size-1).map { x ->
            (0 .. trees.size -1).map { y ->
                scenicScore(trees, x, y)
            }.max()
        }.max()

        println("Part 2 - $result")
    }

    fun scenicScore(trees: List<List<Int>>, x: Int, y: Int): Int {
        val size = trees.size
        val t = trees[x][y]
        var viewBlocked = false
        val upScore = trees.dropLast(size - x).map { it[y] }.takeLastWhile {
            if (viewBlocked) {
                return@takeLastWhile false
            } else if (it < t) {
                return@takeLastWhile true
            } else {
                viewBlocked = true
                return@takeLastWhile true
            }
        }.count()

        viewBlocked = false
        val downScore = trees.drop(x + 1).map { it[y] }.takeWhile {
            if (viewBlocked) {
                return@takeWhile false
            } else if (it < t) {
                return@takeWhile true
            } else {
                viewBlocked = true
                return@takeWhile true
            }
        }.count()

        viewBlocked = false
        val leftScore = trees[x].dropLast(size - y).takeLastWhile {
            if (viewBlocked) {
                return@takeLastWhile false
            } else if (it < t) {
                return@takeLastWhile true
            } else {
                viewBlocked = true
                return@takeLastWhile true
            }
        }.count()

        viewBlocked = false
        val rightScore = trees[x].drop(y + 1).takeWhile {
            if (viewBlocked) {
                return@takeWhile false
            } else if (it < t) {
                return@takeWhile true
            } else {
                viewBlocked = true
                return@takeWhile true
            }

        }.count()

        // println("up: $upScore   left: $leftScore   right: $rightScore   down: $downScore")
        return upScore * downScore * leftScore * rightScore
    }

    fun isVisible(trees: List<List<Int>>, x: Int, y: Int): Boolean {
        val size = trees.size
        val t = trees[x][y]
        if (x == 0 || y == 0 || x == size -1 || y == size - 1) {
            return true
        }

        // top edge visible
        if (trees.dropLast(size - x).map { it[y] }.max() < t ) {
            // println("visible top [$x][$y] $t")
            return true
        }

        // bottom edge visible
        if (trees.drop(x + 1).map { it[y] }.max() < t) {
            // println("visible bottom [$x][$y] $t")
            return true
        }

        // left edge visible
        if (trees[x].dropLast(size - y).max() < t) {
            // println("visible left [$x][$y] $t")
            return true
        }

        // right edge visible
        if (trees[x].drop(y + 1).max() < t) {
            // println("visible right [$x][$y] $t")
            return true
        }

        return false
    }
}