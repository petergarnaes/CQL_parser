# Monadic parser combinators for kotlin

Special thanks to [Sergey Igushkin](https://github.com/h0tk3y/kotlin-monads) and the team of [Graham Hutton and Erik Meijer](https://eprints.nottingham.ac.uk/223/1/pearl.pdf).

This is an implementation of very basic monadic parser combinators.

## Optimizations

 * The `choice` operator could evaluate lazily instead of using the plus operator.
 * List concatenation could be better in `many1` and `sepby1`
 * Better string/char manipulation
 
## Features

 * `choice` could be an operator, but which to choose?
 
 ## TODO
 
  * Make monad implementation dependency on library
  * Make code a package, make example separate package
  * Write proper tests
  * Change repo name on GitHub
  * Publish???