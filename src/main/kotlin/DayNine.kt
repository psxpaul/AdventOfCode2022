import java.lang.IllegalArgumentException
import kotlin.math.abs

class DayNine: AdventDay() {
    override fun partOne(data: List<String>) {
        val headPosition = Position()
        val tailPosition = Position()
        val tailPositions = mutableListOf<Pair<Int, Int>>().also { it.add(headPosition.asPair()) }

        data.forEach {
            val (direction, distance) = it.split(" ").let { Pair(it[0], it[1].toInt()) }
            (1..distance).forEach {
                when (direction) {
                    "R" -> headPosition.x++
                    "L" -> headPosition.x--
                    "U" -> headPosition.y++
                    "D" -> headPosition.y--
                    else -> throw IllegalArgumentException("invalid direction $direction")
                }

                if (headPosition == tailPosition) {
                    // println("equal, do nothing - $headPosition $tailPosition")
                } else if (abs(tailPosition.x - headPosition.x) < 2 && abs(tailPosition.y - headPosition.y) < 2 ) {
                    // println("touching, do nothing - $headPosition $tailPosition")
                } else if ((tailPosition.x - headPosition.x) >= 2) { // 2 steps right of head
                    if (tailPosition.y == headPosition.y) {
                        tailPosition.x--
                    } else if (tailPosition.y > headPosition.y){
                        tailPosition.x--
                        tailPosition.y--
                    } else {
                        tailPosition.x--
                        tailPosition.y++
                    }
                } else if ((headPosition.x - tailPosition.x) >= 2) { // 2 steps left of head
                    if (tailPosition.y == headPosition.y) {
                        tailPosition.x++
                    } else if (tailPosition.y > headPosition.y) {
                        tailPosition.x++
                        tailPosition.y--
                    } else {
                        tailPosition.x++
                        tailPosition.y++
                    }
                } else if ((tailPosition.y - headPosition.y) >= 2) { // 2 steps up of head
                    if (tailPosition.x == headPosition.x) {
                        tailPosition.y--
                    } else if (tailPosition.x > headPosition.x) {
                        tailPosition.y--
                        tailPosition.x--
                    } else {
                        tailPosition.y--
                        tailPosition.x++
                    }
                } else if ((headPosition.y - tailPosition.y) >= 2) { // 2 steps down of head
                    if (tailPosition.x == headPosition.x) {
                        tailPosition.y++
                    } else if (tailPosition.x > headPosition.x) {
                        tailPosition.y++
                        tailPosition.x--
                    } else {
                        tailPosition.y++
                        tailPosition.x++
                    }
                } else {
                    println("doing what now? - $headPosition $tailPosition")
                }
                tailPositions.add(tailPosition.asPair())
            }
        }

        // println(headPosition)
        // println(tailPositions)
        println("Part 1 - ${tailPositions.distinct().size}")
    }

    override fun partTwo(data: List<String>) {
        val knotPositions = (1..10).map { Position() }
        val tailPositions = mutableListOf<Pair<Int, Int>>().also { it.add(knotPositions.last().asPair()) }

        data.forEach { command ->
            val (direction, distance) = command.split(" ").let { Pair(it[0], it[1].toInt()) }
            (1..distance).forEach {
                when (direction) {
                    "R" -> knotPositions[0].x++
                    "L" -> knotPositions[0].x--
                    "U" -> knotPositions[0].y++
                    "D" -> knotPositions[0].y--
                    else -> throw IllegalArgumentException("invalid direction $direction")
                }

                knotPositions.windowed(2, 1, false).forEach {
                    if (it[0] == it[1]) {
                        // println("equal, do nothing - $headPosition $tailPosition")
                    } else if (abs(it[1].x - it[0].x) < 2 && abs(it[1].y - it[0].y) < 2 ) {
                        // println("touching, do nothing - $headPosition $it[1]")
                    } else if ((it[1].x - it[0].x) >= 2) { // 2 steps right of head
                        if (it[1].y == it[0].y) {
                            it[1].x--
                        } else if (it[1].y > it[0].y){
                            it[1].x--
                            it[1].y--
                        } else {
                            it[1].x--
                            it[1].y++
                        }
                    } else if ((it[0].x - it[1].x) >= 2) { // 2 steps left of head
                        if (it[1].y == it[0].y) {
                            it[1].x++
                        } else if (it[1].y > it[0].y) {
                            it[1].x++
                            it[1].y--
                        } else {
                            it[1].x++
                            it[1].y++
                        }
                    } else if ((it[1].y - it[0].y) >= 2) { // 2 steps up of head
                        if (it[1].x == it[0].x) {
                            it[1].y--
                        } else if (it[1].x > it[0].x) {
                            it[1].y--
                            it[1].x--
                        } else {
                            it[1].y--
                            it[1].x++
                        }
                    } else if ((it[0].y - it[1].y) >= 2) { // 2 steps down of head
                        if (it[1].x == it[0].x) {
                            it[1].y++
                        } else if (it[1].x > it[0].x) {
                            it[1].y++
                            it[1].x--
                        } else {
                            it[1].y++
                            it[1].x++
                        }
                    } else {
                        println("doing what now? - ${it[0]} ${it[1]}")
                    }
                }
                tailPositions.add(knotPositions.last().asPair())
            }
        }

        println(knotPositions[0])
        println(tailPositions)
        println("Part 1 - ${tailPositions.distinct().size}")
    }
}

data class Position(
    var x: Int = 0,
    var y: Int = 0,
) {
    fun asPair() = Pair(x, y)
}