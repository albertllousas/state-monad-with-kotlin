package example

import example.HandShape.Paper
import example.HandShape.Rock
import example.HandShape.Scissors
import example.Winner.Player1
import example.Winner.Player2
import example.Winner.Tie
import statemonad.State

sealed class HandShape {
  object Rock : HandShape()
  object Paper : HandShape()
  object Scissors : HandShape()
}

sealed class Winner {
  object Player1 : Winner()
  object Player2 : Winner()
  object Tie : Winner()
}

data class RockPaperScissors(val player1Victories: Int, val player2Victories: Int) {

  companion object {

    val initGame = RockPaperScissors(0, 0)

    fun play(p1Hand: HandShape, p2Hand: HandShape): State<RockPaperScissors, Winner> = State { state ->
      val winner = when (Pair(p1Hand, p2Hand)) {
        Pair(Rock, Scissors) -> Player1
        Pair(Scissors, Paper) -> Player1
        Pair(Paper, Rock) -> Player1
        Pair(Scissors, Rock) -> Player2
        Pair(Paper, Scissors) -> Player2
        Pair(Rock, Paper) -> Player2
        else -> Tie
      }
      val newState = when (winner) {
        Player1 -> state.copy(player1Victories = state.player1Victories.inc())
        Player2 -> state.copy(player2Victories = state.player2Victories.inc())
        Tie -> state
      }
      Pair(winner, newState)
    }

    fun nameWinner(p1Name: String, p2Name: String, winner: Winner): String =
        when (winner) {
          Player1 -> "$p1Name wins!"
          Player2 -> "$p2Name wins!"
          Tie -> "Tie! try again!"
        }
  }
}
