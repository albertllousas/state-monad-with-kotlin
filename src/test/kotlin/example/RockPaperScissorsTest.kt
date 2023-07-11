package example

import arrow.core.Tuple4
import example.HandShape.Paper
import example.HandShape.Rock
import example.HandShape.Scissors
import example.RockPaperScissors.Companion.initGame
import example.RockPaperScissors.Companion.nameWinner
import example.RockPaperScissors.Companion.play
import example.Winner.Player1
import example.Winner.Player2
import example.Winner.Tie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import statemonad.State

class RockPaperScissorsTest {

  @TestFactory
  fun `playing rock paper scissors`() =
      listOf(
          Tuple4(Rock, Scissors, Player1, RockPaperScissors(1, 0)),
          Tuple4(Scissors, Paper, Player1, RockPaperScissors(1, 0)),
          Tuple4(Paper, Rock, Player1, RockPaperScissors(1, 0)),
          Tuple4(Scissors, Rock, Player2, RockPaperScissors(0, 1)),
          Tuple4(Paper, Scissors, Player2, RockPaperScissors(0, 1)),
          Tuple4(Rock, Paper, Player2, RockPaperScissors(0, 1)),
          Tuple4(Rock, Rock, Tie, RockPaperScissors(0, 0)),
          Tuple4(Paper, Paper, Tie, RockPaperScissors(0, 0)),
          Tuple4(Scissors, Scissors, Tie, RockPaperScissors(0, 0)),
      ).map { (p1, p2, winner, newState) ->
        dynamicTest("should play with a resulting winner as $winner when player1 plays $p1 and player2 plays $p2") {
          val state: State<RockPaperScissors, Winner> = play(p1, p2)
          val result: Pair<Winner, RockPaperScissors> = state.run(initGame)

          assertThat(result).isEqualTo(Pair(winner, newState))
        }
      }

  @TestFactory
  fun `naming the winner (using map function)`() =
      listOf(
          Tuple4(Rock, Scissors, "Jane wins!", RockPaperScissors(1, 0)),
          Tuple4(Scissors, Rock, "John wins!", RockPaperScissors(0, 1)),
          Tuple4(Rock, Rock, "Tie! try again!", RockPaperScissors(0, 0))
      ).map { (p1, p2, msg, newState) ->
        dynamicTest("should name the winner as $msg when player1 plays $p1 and player2 plays $p2") {
          val state = play(p1, p2)
          val named = state.map { winner -> nameWinner("Jane", "John", winner) }
          val result = named.run(initGame)

          assertThat(result).isEqualTo(Pair(msg, newState))
        }
      }

  @Test
  fun `should play some rounds preserving the state (using flatmap function)`() {
    val state = play(Scissors, Rock)
        .flatMap { play(Paper, Scissors) }
        .flatMap { play(Paper, Rock) }

    val result = state.run(initGame)

    assertThat(result).isEqualTo(Pair(Player1, RockPaperScissors(1, 2)))
  }
}
