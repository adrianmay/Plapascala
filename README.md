# Plapascala
Play with parsers in scala

Haskellers who think Scala is wussy should read this. 

It's a monadic parser in which for...yield accidentally volunteered itself to act as do...return.

There is a way to define a trait to act like the actual Monad class (at least for bind) but I lost it. It involves a <: constraint on a few parameters.

Other than yield, I'm still stumped for a generic return that the type inference system can complete.


