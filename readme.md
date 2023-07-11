# State monad with kotlin

## Description

- Have you heard about the state monad? 
- Are you wondering what's that? 
- In which cases should you consider it as an option? 

This repo explores and try to answer these questions around this functional programming pattern using kotlin.

## Example using state monad

- [Rock paper scissors game](https://en.wikipedia.org/wiki/Rock_paper_scissors) 
  - [Code](src/main/kotlin/example/RockPaperScissors.kt)
  - [Tests & usage](src/test/kotlin/example/RockPaperScissorsTest.kt)


## Stateful computations

A stateful computation is a function that takes some state and returns a value along with a new state:

`state -> tuple(newState, result)`

Example in kotlin, a function that checks if a number is even and keeps track of all evens already checked:

```kotlin
fn isEven(num: Int, totalOfEvens: Int): Pair<Boolean, Int> = 
  if (num % 2 == 0) Pair(true, totalOfEvens.inc) else Pair(false, totalOfEvens) 
```

What if we abstract the previous function in something more generic?

Let's wrap our initial function in a new type:

```shell
state -> (state, result) to State(f: state -> (state, result))
```

In kotlin:

```kotlin
data class State<STATE, out RESULT>(val run: (STATE) -> Pair<RESULT, STATE>)
```

And let's refactor our previous code:

```kotlin
fun isEven(num: Int): State<Boolean, Int> = State { state -> 
  if (num % 2 == 0) Pair(true, state.inc) else Pair(false, state)
}
```

Simple, state monad is just a **datatype wrapping a stateful computation**

# Wrapping up

We can not finish without mentioning that state is a monad, therefore it should implement `map` and `flatmap` functions.

In this example it would be missing to implement `get` and `put` funtions that usually come alongside the pattern as well.
