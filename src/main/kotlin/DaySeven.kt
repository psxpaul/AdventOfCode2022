class DaySeven: AdventDay() {
    override fun partOne(data: List<String>) {
        val directories = parse(data)
        val result = directories.filter { it.value.size() <= 100000 }.values.sumOf { it.size() }
        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val directories = parse(data)
        val totalUsed = directories["/"]!!.size()
        val unused = 70000000 - totalUsed
        val needed = 30000000 - unused

        val result = directories.filter { it.value.size() >= needed }.minBy { it.value.size() }

        println("totalUsed - $totalUsed")
        println("unused - $unused")
        println("needed - $needed")
        println("Part 2 - ${result.key} - ${result.value.size()}")
    }

    private fun parse(data: List<String>): MutableMap<String, Directory> {
        val currentPath = mutableListOf<String>()
        val directories = mutableMapOf<String, Directory>(
            "/" to Directory("/")
        )

        data.forEach {
            val path = "/${currentPath.joinToString("/")}"

            if (it.startsWith("$")) {
                if (it == "$ cd /") {
                    currentPath.clear()
                } else if (it == "$ cd ..") {
                    currentPath.removeLast()
                } else if (it.startsWith("$ cd ")) {
                    currentPath.add(it.substring(5))
                } else if (it == "$ ls") {
                    // println("listing $path")
                } else {
                    throw RuntimeException("bad command $it")
                }
            } else if (it.startsWith("dir")) {
                val dirName = it.split(" ")[1]
                val newPath = "/${(currentPath + dirName).joinToString("/")}"
                val directory = Directory(dirName)
                directories[newPath] = directory
                directories[path]!!.children.add(directory)
            } else {
                val (size, name) = it.split(" ")
                directories[path]!!.children.add(File(name, size.toInt()))
            }
        }
        return directories
    }
}

sealed class Node {
    abstract fun size(): Int
    abstract fun name(): String
}

data class Directory(
    private val name: String,
    val children: MutableList<Node> = mutableListOf(),
): Node() {
    override fun size() = children.sumOf { it.size() }
    override fun name() = name
}

data class File(private val name: String, private val size: Int): Node() {
    override fun size() = size
    override fun name() = name
}