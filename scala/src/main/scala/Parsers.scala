import scala.util.Try

//
sealed trait Parser[+T]{
  def <|>[U](other: Parser[U]): Parser[Try[Resultado[T]]] ={
    orCombinator(this, other)
  }

//  def <>[U](other: Parser[U]): Parser[(T,U)] ={
//    concatCombinator(this, other)
//  }
//
//  def ~>[U](other: Parser[U]): Parser[T] ={
//    rightmostCombinator(this, other)
//  }
//
//  def <~[U](other: Parser[U]): Parser[U] ={
//    leftmostCombinator(this, other)
//  }

//  def sepBy(sep: Parser[U]): Parser[T] ={
//    sepByCombinator(this, sep)
//  }

  def parse(text: String): Try[Resultado[T]]
}

//case class integer() extends Parser[Int]
case class char(character: Char) extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if (text.head == character) => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}
//case class string(aString: String) extends Parser[String]
//case class orCombinator[T,U](parser1: Parser[T], parser2: Parser[U]) extends Parser[Either[Try[Resultado[T]], Try[Resultado[U]]]]{
//  def parse(text: String): Either[Try[Resultado[T]], Try[Resultado[U]]] = {
//    if (parser1.parse(text).isSuccess) Left(parser1.parse(text)).value else Right(parser2.parse(text))
//  }
//}

case class orCombinator[W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[W]{
  def parse(text: String): Try[Resultado[W]] = {
    if (parser1.parse(text).isSuccess) parser1.parse(text) else parser2.parse(text)
  }
}
//case class concatCombinator[T,U](parser1: Parser[T], parser2: Parser[U]) extends Parser[(T,U)]
//case class rightmostCombinator[T,U](parser1: Parser[T], parser2: Parser[U]) extends Parser[U]
//case class leftmostCombinator[T,U](parser1: Parser[T], parser2: Parser[U]) extends Parser[T]
//case class sepByCombinator[T,U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[List[T]]

//implicit class Parser2(parser: Parser) {
//  def <|>(other: Parser) = (parser, other)
//}

case object pruebitas extends App {
  println(char('c').parse("chau"))
  println(char('c').parse("hau"))
  println((orCombinator2(char('c'), char('h'))).parse("hau"))
  println((orCombinator2(char('c'), char('h'))).parse("cau"))
  println((orCombinator2(char('c'), char('h'))).parse("au"))

//  type Magic[+A, +B] = Either[A, B]
//  val magic: Magic[String,Int] = "Try(Resultado)"
//
////  val l: Either[String, Int] = "String"
//  val l: Either[String, Int] = "a"
}
//case object Digit extends Parser