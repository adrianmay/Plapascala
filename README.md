# Plapascala
Play with parsers in scala

Haskellers who think Scala is wussy should read this. 

It's a monadic parser in which for...yield accidentally volunteered itself to act as do...return.

There is a way to define a trait to act like the actual Monad class (at least for bind) but I lost it. It involves upper-bounding some parameters to the actual trait, which is obviously inherited by the thing that implements it. Other than yield, I'm still stumped for a generic return that the type inference system can complete. Please open an issue if you can suggest the like.


