package parser

import tadp.{ParserErrorException, Resultado, contador, utilities}

import scala.util.{Failure, Success, Try}

abstract class Parser[+T]{
  def <|>[U<:W, W>:T](other: Parser[U]): Parser[W] ={
    orCombinator[W,T,U](this, other)
  }

  def <>[U<:W, W>:T](other: Parser[U]): Parser[List[W]] ={
    concatCombinator[W,T,U](this, other)
  }

  def ~>[U](other: Parser[U]): Parser[U] ={
    rightmostCombinator(this, other)
  }

  def <~[U](other: Parser[U]): Parser[T] ={
    leftmostCombinator(this, other)
  }

  def *(): Parser[List[T]] = {
    clausuraDeKleene[T](this)
  }

  def +(): Parser[List[T]] = {
    clausuraDeKleenePositiva[T](this)
  }

  def satisfies(condition: T => Boolean): Parser[T] = {
    parser.satisfiesCombinator(this)(condition)
  }

  def map[U](mapFunction: T => U): Parser[U] = {
    parser.mapCombinator(this)(mapFunction)
  }

  def sepBy[U](sep: parser.Parser[U]): Parser[List[T]] ={
    parser.sepByCombinator(this, sep)
  }

  def parse(text: String): Try[Resultado[T]]
}

case class anyChar() extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if !text.isEmpty && text.head != ' ' => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class char(character: Char) extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if text.isEmpty => throw new ParserErrorException(Resultado(null, text))
      case _ if text.head == character => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class digit() extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if text.head.isDigit => Resultado(text.head, text.tail)
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class string(string: String) extends Parser[String] {
  def parse(text: String): Try[Resultado[String]] = {
    Try(text match {
      case _ if text.startsWith(string) => {
        contador.inc_id(string)
        Resultado(string, text.substring(string.length))
      }
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class integer() extends Parser[Int] {
  def parse(text: String): Try[Resultado[Int]] = {
    Try("-?\\d+".r.findFirstMatchIn(text) match {
      case Some(i) => Resultado(i.group(0).toInt, text.substring(i.group(0).length))
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class double() extends Parser[Double] {
  def parse(text: String): Try[Resultado[Double]] = {
    Try("-?\\d+(\\.\\d+)?".r.findFirstMatchIn(text) match {
      case Some(i) => Resultado(i.group(0).toDouble, text.substring(i.group(0).length))
      case _ => throw new ParserErrorException(Resultado(null, text))
    })
  }
}

case class orCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[W]{
  def parse(text: String): Try[Resultado[W]] = {
    val p1 = parser1.parse(text)
    if (p1.isSuccess) p1 else parser2.parse(text)
  }
}

case class concatCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[List[W]] {
  def parse(text:String): Try[Resultado[List[W]]] = {
    for {
      result1 <- parser1.parse(text)
      result2 <- parser2.parse(result1.notParsed)
    } yield result2.copy(parsed = utilities.aplanandoAndo[W](result1.parsed, result2.parsed))
  }
}

case class rightmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {
    for {
      result1 <- parser1.parse(text)
      result2 <- parser2.parse(result1.notParsed)
    } yield result2
  }
}

case class leftmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {
    for {
      result1 <- parser1.parse(text)
      result2 <- parser2.parse(result1.notParsed)
    } yield result1.copy(notParsed = result2.notParsed)
  }
}

case class satisfiesCombinator[T](parser: Parser[T])(condition: T => Boolean) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {//: Try[tadp.Resultado[List[T]]]
    val p2 = parser.parse(text)
    p2 match {
        case Failure(e) => Failure(e)
        case success if condition(success.get.parsed) => success
        case _ => Try(throw new ParserErrorException(Resultado(null, text)))
    }
  }
}

case class opt[+T>: None.type](parser: Parser[T]) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {//: Try[tadp.Resultado[List[T]]]
    parser.parse(text) match {
      case Failure(e) => Success(Resultado(None, text))
      case success => success
    }
  }
}

case class clausuraDeKleene[+T](parser: Parser[T]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      uno <- opt(parser <> this).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo[T](uno.parsed))
  }
}

case class clausuraDeKleenePositiva[+T](parser: Parser[T]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      uno <- (parser <> clausuraDeKleene(parser)).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo[T](uno.parsed))
  }
}

case class mapCombinator[T, U](parser: Parser[T])(mapFunction: T => U) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {
    for{
      result <- parser.parse(text)
    } yield result.copy(parsed = mapFunction(result.parsed))
  }
}

//TODO: A VER SI DESPUES NOS ACORDAMOS
case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
//     Anda con la segunda versión del aplanandoAndo, que al final no mejora tanto la performance como pensamos
    val p1 = parserContent.parse(text)
    if(p1.isFailure){
      return Failure(new ParserErrorException[List[T]](Resultado(parsed = List(), notParsed = text)))
    }
    val sep = parserSep.parse(p1.get.notParsed)
    if(sep.isFailure){
      return Success(p1.get.copy(parsed = utilities.aplanandoAndo[T](p1.get.parsed)))
    }
    val larecur = this.parse(sep.get.notParsed)
    if(larecur.isFailure){
      return Success(p1.get.copy(parsed = utilities.aplanandoAndo[T](p1.get.parsed)))
    }
    //123-345
    Try(p1.get.copy(parsed = utilities.aplanandoAndo(p1.get.parsed, larecur.get.parsed), notParsed = larecur.get.notParsed))

    // Anda con la primer versión del aplanandoAndo
//    for {
//      pSep <- (parserContent <> opt(parserSep ~> this) ).parse(text)
//    } yield pSep.copy(parsed = utilities.aplanandoAndo[T](pSep.parsed))

    // Forma ideal hasta que queremos parsear imagenes y tarda mil años
//    for {
//      uno <- ((parserContent <~ parserSep <> this) <|> parserContent).parse(text)
//    } yield uno.copy(parsed = utilities.aplanandoAndo[T](uno.parsed))
  }
}

case class alphaNum() extends Parser[String]{
  def parse(text: String): Try[Resultado[String]] = {
    for {
      result1 <- anyChar().*().parse(text)
    } yield result1.copy(parsed = result1.parsed.mkString)
  }
}

