import java.util.PriorityQueue

class DaySixteen: AdventDay() {
    companion object {
        private val VALVE_REGEX = Regex("^Valve (.+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)$")
        private const val STARTING_ROOM = "AA"
//        private const val TIME_LIMIT = 30
        private const val TIME_LIMIT = 26
    }
    override fun partOne(data: List<String>) {
//        val actionList = parse(data)
//        val result = bestPath(actionList)
//        println("Part 1 - ${result?.pressureReleased()}")
    }

    override fun partTwo(data: List<String>) {
        val actionList = parse(data)
        val paths = bestPaths(actionList).toList().sorted()

        var bestScore = 0
        var bestPair = Pair(0, 0)

        (0 until paths.size-1).forEach { i ->
            (i+1 until paths.size).forEach { j ->
                val myPath = paths[i]
                val elePath = paths[j]
                if (elePath.actions.isNotEmpty() && myPath.openedRooms.intersect(elePath.openedRooms).isEmpty()) {
//                    println("me - ${myPath.pressureReleased()}")
//                    println("elephant - ${elePath.pressureReleased()}")
                    val total = myPath.pressureReleased() + elePath.pressureReleased()
                    if (total > bestScore) {
                        bestScore = total
                        bestPair = Pair(myPath.pressureReleased(), elePath.pressureReleased())
                    } else {
                        return@forEach
                    }
                }
            }
        }

        println("Part 2 - ${bestPair.first} + ${bestPair.second} = $bestScore")
    }

    private fun parse(data: List<String>): ActionList {
        val rooms = mutableMapOf<String, Set<String>>()
        val flowRates = mutableMapOf<String, Int>()
        data.forEach { s ->
            val matches = VALVE_REGEX.find(s)!!.groupValues.drop(1)
            flowRates[matches[0]] = matches[1].toInt()
            rooms[matches[0]] = matches[2].split(", ").toSet()
        }
        val movesBetween = mutableMapOf<Pair<String, String>, List<String>?>()
        rooms.keys.forEach { i ->
            rooms.keys.forEach { j ->
                movesBetween[Pair(i, j)] = shortestPath(i, j, rooms)
            }
        }
        return ActionList(rooms = rooms, flowRates = flowRates, movesBetween = movesBetween)
    }

    private fun shortestPath(src: String, dest: String, rooms: Map<String, Set<String>>): List<String>? {
        val shortestToPos = mutableMapOf(src to 0)
        val previousStepToPos = mutableMapOf<String, String>()
        val queue = mutableListOf(src)
        while (queue.size > 0) {
            var current = queue.removeFirst()
            if (current == dest) {
                val path = mutableListOf<String>()
                while (previousStepToPos.contains(current)) {
                    path.add(0, current)
                    current = previousStepToPos[current]!!
                }
                return path
            }

            rooms[current]!!.forEach {
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

    private fun bestPath(actionList: ActionList): ActionList? {
        val queue = PriorityQueue<ActionList>()
        queue.add(actionList)

        val doneQueue = PriorityQueue<ActionList>()

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.isDone()) {
                doneQueue.add(current)
                continue
            }

            val neighbors = current.neighbors()
            doneQueue.add(current)
            neighbors.filterValues { it != null }.forEach { (nextRoom, nextMoveRooms) ->
                val nextMoves = nextMoveRooms!!.map { Action.Move(it) }
                val nextOpen = Action.Open(nextRoom)
                val next = current.copy(actions = current.actions + nextMoves + nextOpen)
                if (next.isValid()) queue.add(next)
            }
        }

        return doneQueue.peek()
    }

    private fun bestPaths(actionList: ActionList): PriorityQueue<ActionList> {
        val queue = PriorityQueue<ActionList>()
        queue.add(actionList)

        val doneQueue = PriorityQueue<ActionList>()

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.isDone()) {
                doneQueue.add(current)
                continue
            }

            val neighbors = current.neighbors().filterValues { it != null }
            doneQueue.add(current)
            neighbors.forEach { (nextRoom, nextMoveRooms) ->
                val nextMoves = nextMoveRooms!!.map { Action.Move(it) }
                val nextOpen = Action.Open(nextRoom)
                val next = current.copy(actions = current.actions + nextMoves + nextOpen)
                if (next.isValid()) queue.add(next)
            }
        }

        return doneQueue
    }

    data class ActionList(
        val actions: List<Action> = listOf(),
        val rooms: Map<String, Set<String>>,
        val flowRates: Map<String, Int>,
        val movesBetween: Map<Pair<String, String>, List<String>?>,
    ): Comparable<ActionList> {
        private val uselessRooms = flowRates.filter { it.value == 0 }.keys.toSet()
        val openedRooms = actions.filterIsInstance<Action.Open>().map { it.dest }.toSet()

        fun currentRoom() = actions.lastOrNull()?.dest ?: STARTING_ROOM
        fun neighbors() = movesBetween
            .filter { it.key.first == currentRoom() && !uselessRooms.contains(it.key.second) && !openedRooms.contains(it.key.second)}
            .mapKeys { it.key.second }
        fun pressureReleased() = actions.withIndex().filter { it.value is Action.Open }.fold(initial = 0, operation = { acc, openAction ->
            val flowRate = flowRates[openAction.value.dest]!!
            val timeLeft = TIME_LIMIT - openAction.index - 1
            acc + (flowRate * timeLeft)
        })
//        fun pressureReleased() = actions.fold(initial = Pair(setOf<String>(), 0), operation = { acc, action ->
//            val openedValves = if (action is Action.Open) acc.first + action.dest else acc.first
//            Pair(openedValves, acc.second + openedValves.sumOf { flowRates[it]!! })
//        }).second
        fun isValid() = actions.size <= TIME_LIMIT
        fun isDone() = (uselessRooms.size + openedRooms.size) == rooms.keys.size
        override fun compareTo(other: ActionList) = -1 * (pressureReleased().compareTo(other.pressureReleased()))
    }

    sealed interface Action {
        val dest: String

        data class Move(override val dest: String): Action
        data class Open(override val dest: String): Action
    }
}