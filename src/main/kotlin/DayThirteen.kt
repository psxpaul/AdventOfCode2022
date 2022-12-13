class DayThirteen: AdventDay() {
    override fun partOne(data: List<String>) {
        val nodes = parse(data)
        val comparisons = nodes.map { it.first.compareTo(it.second) }
        val correctIndexes = comparisons.withIndex().filter { it.value == -1 }.map { it.index + 1 }
        val result = correctIndexes.sum()
        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val separatorPackets = listOf<PacketNode>(
            PacketNode.ListPacket(listOf(PacketNode.ListPacket(listOf(PacketNode.IntPacket(2))))),
            PacketNode.ListPacket(listOf(PacketNode.ListPacket(listOf(PacketNode.IntPacket(6))))),
        )
        val nodes = parse(data).fold(initial = separatorPackets, operation =  { acc, pair ->
            acc + pair.first + pair.second
        }).sorted()
        val firstIndex = nodes.indexOf(separatorPackets[0])
        val secondIndex = nodes.indexOf(separatorPackets[1])
        val result = (firstIndex + 1) * (secondIndex + 1)
        println("Part 2 - $result")
    }

    private fun parse(data: List<String>): List<Pair<PacketNode, PacketNode>> {
        return data.filter { it.isNotEmpty() }.windowed(2, 2, false).map {
            Pair(
                PacketNode.parse(it[0])!!,
                PacketNode.parse(it[1])!!,
            )
        }
    }
}

sealed class PacketNode: Comparable<PacketNode> {
    data class IntPacket(val value: Int): PacketNode() {
        override fun compareTo(other: PacketNode) = when (other) {
            is IntPacket -> value.compareTo(other.value)
            is ListPacket -> ListPacket(listOf(this)).compareTo(other)
        }
    }

    data class ListPacket(val values: List<PacketNode>): PacketNode() {
        override fun compareTo(other: PacketNode): Int {
            val otherVals = if (other is IntPacket) listOf(other) else (other as ListPacket).values
            val minSize = minOf(values.size, otherVals.size)
            (0 until minSize).forEach { i ->
                val itemComp = values[i].compareTo(otherVals[i])
                if (itemComp != 0) return itemComp
            }
            return if (values.size == otherVals.size) 0
            else if (minSize == values.size) -1
            else 1
        }
    }

    companion object {
        fun parse(input: String): PacketNode? {
            return if (input.startsWith("[")) {
                val children = input.substring(1, input.lastIndexOf(']'))
                    .split(",")
                    .fold(initial = listOf<String>(), operation = { acc, s ->
                        if (acc.isNotEmpty() &&
                            acc.last().count { it == '[' } != acc.last().count { it == ']' }) {
                            acc.dropLast(1) + (acc.last() + "," + s)
                        } else {
                            acc + s
                        }
                    })

                ListPacket(children.mapNotNull { parse(it) })
            } else if (input.isEmpty()) {
                return null
            } else {
                IntPacket(input.toInt())
            }
        }
    }
}