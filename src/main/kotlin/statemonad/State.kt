package statemonad

data class State<STATE, out RESULT>(val run: (STATE) -> Pair<RESULT, STATE>) {

  fun <B> map(fn: (RESULT) -> B): State<STATE, B> =
      State { state: STATE ->
        val (result, newState) = run(state)
        Pair(fn(result), newState)
      }

  fun <B> flatMap(fn: (RESULT) -> State<STATE, B>): State<STATE, B> =
      State { state: STATE ->
        val (result, newState) = run(state)
        fn(result).run(newState)
      }
}
