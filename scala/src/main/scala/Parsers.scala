import scala.util.Try

//
sealed trait Parser[+T]{
  def <|>[U](other: Parser[U]): orCombinator[Any, T, U] ={
    orCombinator(this, other)
  }

  def <>[U](other: Parser[U]): Parser[(T,U)] ={
    concatCombinator(this, other)
  }

  def ~>[U](other: Parser[U]): rightmostCombinator[T, U] ={
    rightmostCombinator(this, other)
  }

  def <~[U](other: Parser[U]): leftmostCombinator[T, U] ={
    leftmostCombinator(this, other)
  }

//  def sepBy(sep: Parser[U]): Parser[T] ={
//    sepByCombinator(this, sep)
//  }

  def parse(text: String): Try[Resultado[T]]
}

case class anyChar() extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if (!text.isEmpty) => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class char(character: Char) extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if (text.isEmpty) => throw new ParserErrorException(Resultado(null, text))
      case _ if (text.head == character) => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class digit() extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if (text.head.isDigit) => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class string(string: String) extends Parser[String] {
  def parse(text: String): Try[Resultado[String]] = {
    Try(text match {
      case _ if (text.startsWith(string)) => Resultado(string, text.substring(string.length))
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class integer() extends Parser[Int] {
  def parse(text: String): Try[Resultado[Int]] = {
    Try("\\-?\\d+".r.findFirstMatchIn(text) match {
      case Some(i) => Resultado(i.group(0).toInt, text.substring(i.group(0).length))
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class double() extends Parser[Double] {
  def parse(text: String): Try[Resultado[Double]] = {
    Try("\\-?\\d+(\\.\\d+)?".r.findFirstMatchIn(text) match {
      case Some(i) => Resultado(i.group(0).toDouble, text.substring(i.group(0).length))
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class orCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[W]{
  def parse(text: String): Try[Resultado[W]] = {
    if (parser1.parse(text).isSuccess) parser1.parse(text) else parser2.parse(text)
  }
}

case class concatCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[(T,U)] {
  def parse(text:String): Try[Resultado[(T,U)]] = {
    for {
      result1 <- parser1.parse(text) // Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2.copy(parsed = (result1.parsed, result2.parsed))
  }
}

case class rightmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {
    for {
      result1 <- parser1.parse(text) // Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2
  }
}

case class leftmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {
    for {
      result1 <- parser1.parse(text) // Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result1.copy(notParsed = result2.notParsed)
  }
}

case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      result1 <- parserContent.parse(text)
      result2 <- parserSep.parse(result1.notParsed)
    } yield result1.copy(parsed = List(result1.parsed), "")// notParsed=result2.notParsed
  }
  
}

//implicit class Parser2(parser: Parser) {
//  def <|>(other: Parser) = (parser, other)
//}

case object pruebitas extends App {
  println(char('c').parse("chau"))
  println(char('c').parse("hau"))
  println((char('c') <|> char('o')).parse("cola"))
  println(orCombinator(char('c'), char('h')).parse("hau"))
  println((orCombinator(char('c'), char('h'))).parse("cau"))
  println((orCombinator(char('c'), char('h'))).parse("au"))
  println(integer().parse("hau"))
  println(integer().parse("123.asd"))
  println(integer().parse("-43534543"))
  println(double().parse("hau"))
  println(double().parse("123.asd"))
  println(double().parse("-4353.4543"))

  println(sepByCombinator(integer(),char('-')).parse("123-"))
  println(sepByCombinator(integer(),char('-')).parse(""))
  println(sepByCombinator(integer(),char('-')).parse("123"))
  //  type Magic[+A, +B] = Either[A, B]
//  val magic: Magic[String,Int] = "Try(Resultado)"
//
////  val l: Either[String, Int] = "String"
//  val l: Either[String, Int] = "a"
}
//case object Digit extends Parser