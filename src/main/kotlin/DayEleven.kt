import java.lang.IllegalArgumentException

class DayEleven: AdventDay() {
    override fun partOne(data: List<String>) {
        val monkeys = parse(data)
        (1..20).forEach { round ->
            monkeys.withIndex().forEach { (i, monkey) ->
//                println("Monkey $i:")
                monkey.items.forEach { item ->
                    val newItem = monkey.operation.eval(item)
                    val boredItem = newItem / 3
                    val testsTrue = monkey.test.eval(boredItem)
                    val destination = if (testsTrue) monkey.trueDestination else monkey.falseDestination

//                    println("  Monkey inspects an item with a worry level of $item.")
//                    println("    Worry level ${monkey.operation.describe()} to $newItem.")
//                    println("    Monkey gets bored with item. Worry level is divided by 3 to $boredItem.")
//                    println("    Current worry level is${if (!testsTrue) " not " else " "}divisible by ${monkey.test.amount}.")
//                    println("    Item with worry level $boredItem is thrown to monkey $destination.")

                    monkeys[destination].items.add(boredItem)
                    monkey.inspectionCount++
                }
                monkey.items.clear()
            }
//            println("After round $round, the monkeys are holding items with these worry levels:")
//            monkeys.withIndex().forEach { (i, monkey) ->
//                println("Monkey $i: ${monkey.items.joinToString(", ")}")
//            }
        }

//        monkeys.withIndex().forEach { (i, monkey) ->
//            println("Monkey $i inspected items ${monkey.inspectionCount} times.")
//        }
        val result = monkeys.map { it.inspectionCount }.sorted().takeLast(2).reduce { a, b -> a * b }
        println("Part 1 - $result")
    }

    override fun partTwo(data: List<String>) {
        val monkeys = parse(data)
        val truncateAmount = monkeys.map { it.test.amount }.reduce { a, b -> a * b }
        (1..10000).forEach { round ->
            monkeys.withIndex().forEach { (i, monkey) ->
                monkey.items.forEach { item ->
                    val newItem = monkey.operation.eval(item) % truncateAmount
                    val testsTrue = monkey.test.eval(newItem)
                    val destination = if (testsTrue) monkey.trueDestination else monkey.falseDestination
                    monkeys[destination].items.add(newItem)
                    monkey.inspectionCount++
                }
                monkey.items.clear()
            }
//            println("After round $round, the monkeys are holding items with these worry levels:")
//            monkeys.withIndex().forEach { (i, monkey) ->
//                println("Monkey $i: ${monkey.items.joinToString(", ")}")
//            }
        }

        monkeys.withIndex().forEach { (i, monkey) ->
            println("Monkey $i inspected items ${monkey.inspectionCount} times.")
        }
        val result = monkeys.map { it.inspectionCount }.sorted().takeLast(2).reduce { a, b -> a * b }
        println("Part 2 - $result")
    }

    fun parse(data: List<String>) = data.joinToString("\n").split("\n\n").map {
        val attributes = it.split("\n")

        Monkey(
            attributes[1].replace("  Starting items: ", "").split(", ").map { it.toLong() }.toMutableList(),
            Operation.from(attributes[2].replace("  Operation: new = old ", "").split(" ")),
            Test(attributes[3].replace("  Test: divisible by ", "").toLong()),
            attributes[4].replace("    If true: throw to monkey ", "").toInt(),
            attributes[5].replace("    If false: throw to monkey ", "").toInt(),
        )
    }
}

data class Monkey(
    val items: MutableList<Long> = mutableListOf(),
    val operation: Operation,
    val test: Test,
    val trueDestination: Int,
    val falseDestination: Int,
    var inspectionCount:Long = 0,
)

sealed class Operation {
    abstract fun eval(input: Long): Long
    abstract fun describe(): String

    class Multiply(val amount: Long): Operation() {
        override fun eval(input: Long) = input * amount
        override fun describe() = "is multiplied by $amount"

    }
    class Add(val amount: Long): Operation() {
        override fun eval(input: Long) = input + amount
        override fun describe() = "increases by $amount"
    }

    class Square : Operation() {
        override fun eval(input: Long) = input * input
        override fun describe() = "is multiplied by itself"
    }

    companion object {
        fun from(input: List<String>) = if (input[0] == "*" && input[1] == "old") Square() else
            when (input[0]) {
                "*" -> Multiply(input[1].toLong())
                "+" -> Add(input[1].toLong())
                else -> throw IllegalArgumentException("invalid operation: ${input[0]}")
            }
    }
}

data class Test(val amount: Long) {
    fun eval(input: Long) = input % amount == 0L
}