class DayTwo: AdventDay() {
    override fun partOne(data: List<String>) {
        var totalScore = 0
        data.forEach {
            val moves = it.split(' ')
            val opponentMove = Move.fromOpponent(moves[0])
            val myMove = Move.fromMy(moves[1])
            val isWin = myMove.isWin(opponentMove)
            // val result = when (isWin) {
            //     true -> "WIN"
            //     false -> "LOSE"
            //     null -> "DRAW"
            // }
            val score = myMove.score + when (isWin) {
                true -> 6
                false -> 0
                null -> 3
            }
            // println("$opponentMove $myMove - $result : $score")
            totalScore += score
        }
        println("Part1 totalScore - $totalScore")
    }

    override fun partTwo(data: List<String>) {
        var totalScore = 0
        data.forEach {
            val moves = it.split(' ')
            val opponentMove = Move.fromOpponent(moves[0])
            val desiredResult = DesiredResult.from(moves[1])
            val myMove = desiredResult.toMove(opponentMove)
            val score = myMove.score + when (desiredResult) {
                DesiredResult.WIN -> 6
                DesiredResult.LOSE -> 0
                DesiredResult.DRAW -> 3
            }

            // println("$it  -  $opponentMove $desiredResult $myMove $score")
            totalScore += score
        }

        println("Part2 totalScore - $totalScore")
    }
}

enum class Move(val oppStr: String, val myStr: String, val score: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3);

    companion object {
        fun fromOpponent(s: String) = values().find { it.oppStr == s }!!
        fun fromMy(s: String) = values().find { it.myStr == s }!!
    }

    fun isWin(other: Move) = when (this) {
        ROCK -> when (other) {
            ROCK -> null
            PAPER -> false
            SCISSORS -> true
        }
        PAPER -> when (other) {
            ROCK -> true
            PAPER -> null
            SCISSORS -> false
        }
        SCISSORS -> when (other) {
            ROCK -> false
            PAPER -> true
            SCISSORS -> null
        }
    }
}

enum class DesiredResult(val s: String) {
    WIN("Z"),
    DRAW("Y"),
    LOSE("X");

    companion object {
        fun from(s: String) = values().find { it.s == s }!!
    }
    fun toMove(oppMove: Move) = when (oppMove) {
        Move.ROCK -> when (this) {
            WIN -> Move.PAPER
            LOSE -> Move.SCISSORS
            DRAW -> Move.ROCK
        }
        Move.PAPER -> when (this) {
            WIN -> Move.SCISSORS
            LOSE -> Move.ROCK
            DRAW -> Move.PAPER
        }
        Move.SCISSORS -> when (this) {
            WIN -> Move.ROCK
            LOSE -> Move.PAPER
            DRAW -> Move.SCISSORS
        }
    }
}
