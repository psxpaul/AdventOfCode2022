import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs

class DayFifteen: AdventDay() {
    companion object {
        private val SENSOR_REGEX = Regex("^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$")
        private val TARGET_ROW = 2000000
        private val MAX_SIZE = 4000000
//        private val TARGET_ROW = 10
//        private val MAX_SIZE = 20
    }

    override fun partOne(data: List<String>) {
//        val cave = parse(data).also { it.fillUnknowns() }
//        val result = cave.positions.filter { it.key.y == TARGET_ROW && it.value == CaveState.Empty }.size
//
//        println("Part 1 - $result")
    }

    fun validPoint(pos: Position) = pos.x in 0..MAX_SIZE && pos.y in 0 .. DayFifteen.Companion.MAX_SIZE

    override fun partTwo(data: List<String>) {
        val cave = parse(data)
        val sensors = cave.positions.filter { it.value is CaveState.Sensor }
        val beacons = cave.positions.filter { it.value is CaveState.Beacon }.keys

        println("Part 2 - ")
        sensors.forEach { (sensorPos, sensor) ->
            val distanceToBeacon = (sensor as CaveState.Sensor).distanceToBeacon
            (0..distanceToBeacon).forEach { i ->
                val top = Position(sensorPos.x + i, sensorPos.y + distanceToBeacon + 1 + i)
                val bottom = Position(sensorPos.x - i, sensorPos.y - distanceToBeacon - 1 - i)
                val left = Position(sensorPos.x + i, sensorPos.y + distanceToBeacon + 1 - i)
                val right = Position(sensorPos.x - i, sensorPos.y + distanceToBeacon - 1 + i)

                if (validPoint(top) && !beacons.contains(top) && sensors.all { (key, value) -> top.distanceTo(key) > ((value as CaveState.Sensor).distanceToBeacon) }) {
                    println("top - $top     ${top.frequency()}")
                }
                if (validPoint(bottom) && !beacons.contains(bottom) && sensors.all { (key, value) -> bottom.distanceTo(key) > ((value as CaveState.Sensor).distanceToBeacon) }) {
                    println("bottom - $bottom     ${bottom.frequency()}")
                }
                if (validPoint(left) && !beacons.contains(left) && sensors.all { (key, value) -> left.distanceTo(key) > ((value as CaveState.Sensor).distanceToBeacon) }) {
                    println("left - $left     ${left.frequency()}")
                }
                if (validPoint(right) && !beacons.contains(right) && sensors.all { (key, value) -> right.distanceTo(key) > ((value as CaveState.Sensor).distanceToBeacon) }) {
                    println("right - $right     ${right.frequency()}")
                }
            }
        }
    }

    private fun parse(data: List<String>) =
        data.fold(initial = Cave(), operation = { cave, s ->
            val positions = SENSOR_REGEX.find(s)!!.groupValues.drop(1).map { it.toInt() }
            val sensorPos = Position(positions[0], positions[1])
            val beaconPos = Position(positions[2], positions[3])
            cave.positions[sensorPos] = CaveState.Sensor(sensorPos.distanceTo(beaconPos))
            cave.positions[beaconPos] = CaveState.Beacon
            cave.minX = minOf(sensorPos.x, beaconPos.x, cave.minX)
            cave.maxX = maxOf(sensorPos.x, beaconPos.x, cave.maxX)
            cave.minY = minOf(sensorPos.y, beaconPos.y, cave.minY)
            cave.maxY = maxOf(sensorPos.y, beaconPos.y, cave.maxY)
            cave
        })

    data class Cave(
        val positions: MutableMap<Position, CaveState> = mutableMapOf(),
        var minX: Int = Int.MAX_VALUE,
        var maxX: Int = Int.MIN_VALUE,
        var minY: Int = Int.MAX_VALUE,
        var maxY: Int = Int.MIN_VALUE,
    ) {
        fun fillUnknowns() {
            val sensors = positions.entries.filter { it.value is CaveState.Sensor }
//            minX -= sensors.filter { it.key.x == minX }.maxOfOrNull { (it.value as CaveState.Sensor).distanceToBeacon } ?: 1
//            maxX -= sensors.filter { it.key.x == maxX }.maxOfOrNull { (it.value as CaveState.Sensor).distanceToBeacon } ?: 1
//            minY -= sensors.filter { it.key.x == minY }.maxOfOrNull { (it.value as CaveState.Sensor).distanceToBeacon } ?: 1
//            maxY -= sensors.filter { it.key.x == maxY }.maxOfOrNull { (it.value as CaveState.Sensor).distanceToBeacon } ?: 1

            sensors.forEach {
                val sensorPos = it.key
                val sensor = it.value as CaveState.Sensor

                ((it.key.x - sensor.distanceToBeacon) .. (it.key.x + sensor.distanceToBeacon)).forEach { x ->
                    val pos = Position(x, TARGET_ROW)
                    if (sensors.any { sensorPos.distanceTo(pos) <= (sensor.distanceToBeacon) }) {
                        positions.putIfAbsent(pos, CaveState.Empty)
                    }
                }
            }

//            (0 .. MAX_SIZE).forEach { y ->
//                (0..MAX_SIZE).forEach { x ->
//                    val pos = Position(x, y)
//                    if (positions.contains(pos)) {
//                        // do not alter existing entries
//                    } else if (sensors.any { it.key.distanceTo(pos) <= ((it.value as CaveState.Sensor).distanceToBeacon) }) {
////                        positions.putIfAbsent(pos, CaveState.Empty)
//                    } else {
////                        positions.putIfAbsent(pos, CaveState.Unknown)
//                        println("$x $y")
//                    }
//                }
//            }
        }

        override fun toString(): String {
            var output = "\n"
//            (minY .. maxY).forEach { y ->
//                (minX .. maxX).forEach { x ->
//                    output += (positions[Position(x, y)]?.s ?: '.')
//                }
//                output += '\n'
//            }
            return output
        }
    }

    sealed class CaveState(val s: Char) {
        object Empty: CaveState('#')
        data class Sensor(val distanceToBeacon: Int): CaveState('S')
        object Beacon: CaveState('B')
        object Unknown: CaveState('.')
    }
}

fun Position.distanceTo(p1: Position) = abs(p1.x - x) + abs(p1.y - y)
fun Position.frequency() = (BigInteger.valueOf(x.toLong()) * BigInteger.valueOf(4000000L)) + BigInteger.valueOf(y.toLong())
