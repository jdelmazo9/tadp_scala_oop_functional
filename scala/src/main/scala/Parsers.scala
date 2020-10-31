

//
sealed trait Parser{
  def <|>(other: Parser): Parser ={
    orCombinator(this, other)
  }

  def <>(other: Parser): Parser ={
    concatCombinator(this, other)
  }

  def ~>(other: Parser): Parser ={
    rightmostCombinator(this, other)
  }

  def <~(other: Parser): Parser ={
    leftmostCombinator(this, other)
  }

  def sepBy(sep: Parser): Parser ={
    sepByCombinator(this, sep)
  }
}

case class integer() extends Parser
//case object integer extends Parser
case class char(character: Char) extends Parser
case class string(aString: String) extends Parser
case class orCombinator(parser1: Parser, parser2: Parser) extends Parser
case class concatCombinator(parser1: Parser, parser2: Parser) extends Parser
case class rightmostCombinator(parser1: Parser, parser2: Parser) extends Parser
case class leftmostCombinator(parser1: Parser, parser2: Parser) extends Parser
case class sepByCombinator(parserContent: Parser, parserSep: Parser) extends Parser

//implicit class Parser2(parser: Parser) {
//  def <|>(other: Parser) = (parser, other)
//}


//case object Digit extends Parser