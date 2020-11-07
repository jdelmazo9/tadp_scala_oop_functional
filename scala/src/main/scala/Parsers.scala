import Dibujante.dibujar
import scalafx.scene.paint.Color
import tadp.{ParserErrorException, Punto, Resultado, utilities}
import tadp.internal.TADPDrawingAdapter
import tree.{Colorete, Cuadrado, Grupo}

import scala.util.{Failure, Success, Try}

sealed trait Parser[+T]{
  def <|>[U<:W, W>:T](other: Parser[U]): orCombinator[W, T, U] ={
    orCombinator[W,T,U](this, other)
  }

  def <>[U<:W, W>:T](other: Parser[U]): concatCombinator[W,T,U] ={
    concatCombinator[W,T,U](this, other)
  }

  def ~>[U](other: Parser[U]): rightmostCombinator[T, U] ={
    rightmostCombinator(this, other)
  }

  def <~[U](other: Parser[U]): leftmostCombinator[T, U] ={
    leftmostCombinator(this, other)
  }

  def *(): clausuraDeKleene[T] = {
    clausuraDeKleene[T](this)
  }

  def +(): clausuraDeKleenePositiva[T] = {
    clausuraDeKleenePositiva[T](this)
  }

//  def satisfies():
//  def map[U](mapFunction: T => U): mapCombinator[T,U] = {
//    mapCombinator(this)(mapFunction)
//  }


  //  def sepBy(sep: Parser[U]): sepByCombinator[T, Any] ={
//    sepByCombinator(this, sep)
//  }

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
      case _ if text.startsWith(string) => Resultado(string, text.substring(string.length))
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
    if (parser1.parse(text).isSuccess) parser1.parse(text) else parser2.parse(text)
  }
}
// 1? <> 5*
//case class concatCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[Product] {
case class concatCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[List[W]] {
  def parse(text:String): Try[Resultado[List[W]]] = {
    for {
      result1 <- parser1.parse(text) // tadp.Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2.copy(parsed = utilities.aplanandoAndo(result1.parsed, result2.parsed))
  }
}

case class rightmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {
    for {
      result1 <- parser1.parse(text) // tadp.Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2
  }
}

case class leftmostCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {
    for {
      result1 <- parser1.parse(text) // tadp.Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result1.copy(notParsed = result2.notParsed)
  }
}

//123-456
//val numeroDeTelefono "123-456" = integer.sepBy(char('-'))("123-456")


// 123                  Cont
// 123-456              Cont Sep Cont
// 123-456-789          Cont Sep Cont Sep Cont
// 123-456-789-100      Cont Sep Cont Sep Cont Sep Cont
// 123-.........-121

//CHECK DEL ANY PARA LA FUNCION CONDITION => TODO: Debería ser T y tira varianza y contravarianza
case class satisfies[T](parser: Parser[T])(condition: T => Boolean) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {//: Try[tadp.Resultado[List[T]]]
    parser.parse(text) match {
        case Failure(e) => Failure(e)
        case success if condition(success.get.parsed) => success
        case _ => Try(throw new ParserErrorException(Resultado(null, text)))
    }
//    for {
//      result <- parser.parse(text)
//      if condition(result.parsed)
//    } yield result
  }
}

//TODO: OTRA IDEA ES HACER EL PARSER QUE NO PARSEA NADA (EL FALSO PARSER)
case class opt[+T>: None.type](parser: Parser[T]) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {//: Try[tadp.Resultado[List[T]]]
    parser.parse(text) match {
//      case Failure(e) => Success(tadp.Resultado(parsed = null, notParsed = text))
      case Failure(e) => Success(Resultado(None, text))
      case success => success
    }
  }
}

//*: la clausura de Kleene se aplica a un parser, convirtiéndolo en otro que se puede aplicar todas las veces que sea posible o 0 veces. El resultado debería ser una lista que contiene todos los valores que hayan sido parseados (podría no haber ninguno).
case class clausuraDeKleene[+T](parser: Parser[T]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      uno <- opt(parser <> this).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}
//
////  +: es como la clausura de Kleene pero requiere que el parser se aplique al menos UNA vez.
case class clausuraDeKleenePositiva[+T](parser: Parser[T]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      uno <- (parser <> clausuraDeKleene(parser)).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

case class mapCombinator[T, U](parser: Parser[T])(mapFunction: T => U) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {//: Try[tadp.Resultado[List[T]]]
    for{
      result <- parser.parse(text)
    } yield result.copy(parsed = mapFunction(result.parsed))
  }
}

case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {//: Try[tadp.Resultado[List[T]]]
    for {
      uno <- ((parserContent <~ parserSep <> this) <|> parserContent).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

case object parserEspacios extends Parser[List[Char]] {
  def parse(text: String): Try[Resultado[List[Char]]] = {
//    (satisfies(anyChar())(_.isWhitespace)).*.parse(text)
    (char(' ') <|> char('\n') <|> char('\t')).*.parse(text)
  } //Claúsula de kleene con n cantidad de espacios
}



case object parserPunto extends Parser[Punto] {
  def parse(text: String): Try[Resultado[Punto]] = {
    val parserPartes = (double() <~ parserEspacios <~ string("@")) <> (parserEspacios ~> double())
    parserPartes.parse(text).map(resultado => resultado.parsed match {
      case List(a,b) => Resultado(Punto(a,b), resultado.notParsed)
    })
  }
}


case class alphaNum() extends Parser[String]{
  def parse(text: String): Try[Resultado[String]] = {
    for {
      result1 <- anyChar().*().parse(text)
    } yield result1.copy(parsed = result1.parsed.mkString )

  }
}

case object parserCuadrado extends Parser[Cuadrado] {
  def parse(text: String): Try[Resultado[Cuadrado]] = {
    val parserPartes = string("cuadrado[") ~> (parserPunto <> (char(',') ~> parserPunto)) <~ char(']')
    parserPartes.parse(text).map(resultado => resultado.parsed match {
      case List(a,b) => Resultado(Cuadrado(a,b), resultado.notParsed)
    })
  }
}

//case object parserColor extends Parser[Colorete] {
//  def parse(text: String): Try[Resultado[Colorete]] = {
//    val parserPartes = string("color[") ~> (integer() <> (char(',') ~> integer()) <> (char(',') ~> integer()) ) <~ char(']')
//    parserPartes.parse(text).map(resultado => resultado.parsed match {
//      case List(a,b) => Resultado(Cuadrado(a,b), resultado.notParsed)
//    })
//  }
//}


