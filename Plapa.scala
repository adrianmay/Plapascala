object Plapa {

	// Monadic parser: takes a string (stream would be better) of grammar 
	//   and returns a list of parsing theories,
	//	 each theory being a tuple of the thing found and the rest of the string
	type ParseF[A] = String => List[(A, String)]

	// Wrap the parser function in a monad so we can specialise acordingly
	case class ParseM[A] ( f : ParseF[A] ) {
		
		// Run the parser, choosing any surviving theory
		def parse( s : String ) = f(s).headOption

		// Sequence two parsers to make a compound production
		//	 Analogous to Haskell's bind
		def flatMap[B] ( g : A => ParseM[B] ) = ParseM[B] ( 
		 	s => for { (a, s1) <- f(s); (b, s2) <- g(a).f(s1) } yield (b, s2) ) 

		//Implement Functor and hope for...yield becomes Haskell's do...return	
		def map[B] ( g : A => B ) = ParseM[B] ( 
		 	s => for { (a, s1) <- f(s) } yield (g(a), s1) ) 

		// Options in the grammar
		def or ( g : ParseM[A] ) = ParseM[A] ( 
		 	s => f(s) ::: g.f(s)) 

		// Repeat a production, folding the results
		def many[B] (sofar: B, op: A => B => B) : ParseM[B] = 
			flatMap ( d => many( op(d)(sofar), op ) ) or returnP(sofar)

	}

	// Haskell's return for this monad. 
	//	 Makes a parser that eats no input 
	//	 and returns the parameter unconditionally
	def returnP[A] ( a : A ) = ParseM[A] ( s => List( (a,s) ) )
  
	// Just enabling (car:cdr)-style matching of strings
	object cons { def unapply(s: String): Option[(Char, String)] = s.headOption.map{ (_, s.tail) } }

	def oneCharSatisfying( test : Char => Boolean ) = ParseM[Char] ( 
		s => s match { 
			case h cons t if test(h) => List( (h,t) )
			case _ => Nil
		}
	) 

	// Finally, we can build some parsers
	val digit = oneCharSatisfying ( (c:Char) => c>='0' && c<='9' )
	val lower = oneCharSatisfying ( (c:Char) => c>='a' && c<='z' )
	val upper = oneCharSatisfying ( (c:Char) => c>='A' && c<='z' )

	val letter   = upper or lower
	val location = for { l <- letter; d <-digit } yield List(d,l).mkString
	val number   = digit.many( 0, (i:Char) => (n:Int) => n*10 + i.asDigit )

	val grammar  = for { l <- location; n <- number } yield (n,l)

	def main(args: Array[String]): Unit = {
		println( grammar.parse("B29876and the rest"))
		// Output: Some(((9876,2B),and the rest))
	}

}


