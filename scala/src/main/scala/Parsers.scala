import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter

import scala.util.{Failure, Success, Try}

//println(utilities.aplanandoAndo(List(1,2)))

case object utilities {
  def aplanandoAndo[T<: Any](algoAplanableONoTanto: Any): List[T] =
    algoAplanableONoTanto match {
      case None => List()
      case (a, b) => (aplanandoAndo(a) ++ aplanandoAndo(b)).asInstanceOf[List[T]]
      case List(a :: b) => (a :: aplanandoAndo(b)).asInstanceOf[List[T]]
      case a :: b => (a :: aplanandoAndo(b)).asInstanceOf[List[T]]
      case a if a != Nil => List(a).asInstanceOf[List[T]]
      case _ => List()
    }

//  def aplanandoAndoUnitipado[T](algo: (T, List[T])): List[T] = {
//    algo match {
//      case (a,List()) => List(a)
//      case None => List()
//      case (a,b) => a :: aplanandoAndoUnitipado(b,List(b))
//      case List(a::b) => a :: aplanandoAndoUnitipado(b)
//      case a::b => a :: aplanandoAndoUnitipado(b)
//      case a if a != Nil => List(a)
//      case _ => List()
//    }
//  }

//  val tuplaDe: Product => Product = {
//    case ((a,b),c) => Tuple3(a,b,c)
//    case ((a,b,c),d) => Tuple4(a,b,c,d)
//    case (a,None) => Tuple1(a)
//    case (None, b) => Tuple1(b)
//    case (a, List(None)) => Tuple1(a)
//    case (List(None), b) => Tuple1(b)
//    case loquevenga => loquevenga
//  }

  val tuplaDe: Product => List[Any] = {
    aplanandoAndo(_)
  }
}

//def aplanandoLaTupla(tuple: Any): List[Any] = {
//  tuple match {
//    case (a,b) => (a :: aplanandoLaTupla(b))
//    case _ => List(tuple)
//  }
//}
//
sealed trait Parser[+T]{
  def <|>[U](other: Parser[U]): orCombinator[Any, T, U] ={
    orCombinator(this, other)
  }
//A >: Bicycle <: Vehicle
  //case class concatCombinator[+W,+T<:W,+U<:W](parser1: Parser[T], parser2: Parser[U]) extends Parser[List[W]] {
  def <>[U<:W, W>:T](other: Parser[U]): concatCombinator[W,T,U] ={
    concatCombinator(this, other)
  }

  def ~>[U](other: Parser[U]): rightmostCombinator[T, U] ={
    rightmostCombinator(this, other)
  }

  def <~[U](other: Parser[U]): leftmostCombinator[T, U] ={
    leftmostCombinator(this, other)
  }

  def *(): clausuraDeKleene[Any] = {
    clausuraDeKleene(this)
  }

  def +(): clausuraDeKleenePositiva[Any] = {
    clausuraDeKleenePositiva(this)
  }

//  def satisfies():
//  def map[U](mapFunction: T => U): mapCombinator[T,U] = {
//    mapCombinator(this, mapFunction)
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
      result1 <- parser1.parse(text) // Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2.copy(parsed = utilities.aplanandoAndo(result1.parsed, result2.parsed))
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

//123-456
//val numeroDeTelefono "123-456" = integer.sepBy(char('-'))("123-456")


// 123                  Cont
// 123-456              Cont Sep Cont
// 123-456-789          Cont Sep Cont Sep Cont
// 123-456-789-100      Cont Sep Cont Sep Cont Sep Cont
// 123-.........-121

//CHECK DEL ANY PARA LA FUNCION CONDITION => TODO: Debería ser T y tira varianza y contravarianza
case class satisfies[T](parser: Parser[T])(condition: T => Boolean) extends Parser[T] {
  def parse(text:String): Try[Resultado[T]] = {//: Try[Resultado[List[T]]]
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
  def parse(text:String): Try[Resultado[T]] = {//: Try[Resultado[List[T]]]
    parser.parse(text) match {
//      case Failure(e) => Success(Resultado(parsed = null, notParsed = text))
      case Failure(e) => Success(Resultado(None, text))
      case success => success
    }
  }
}

//*: la clausura de Kleene se aplica a un parser, convirtiéndolo en otro que se puede aplicar todas las veces que sea posible o 0 veces. El resultado debería ser una lista que contiene todos los valores que hayan sido parseados (podría no haber ninguno).
case class clausuraDeKleene[+T >: Null](parser: Parser[T]) extends Parser[List[T]] {
  def parse(text:String): Try[Resultado[List[T]]] = {
    for {
      uno <- opt(parser <> this).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed).asInstanceOf[List[T]])
  }
}
//
////  +: es como la clausura de Kleene pero requiere que el parser se aplique al menos UNA vez.
case class clausuraDeKleenePositiva[+T >: Null](parser: Parser[T]) extends Parser[List[Any]] {
  def parse(text:String): Try[Resultado[List[Any]]] = {
    for {
      uno <- (parser <> clausuraDeKleene(parser)).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

case class mapCombinator[T, U](parser: Parser[T], mapFunction: T => U) extends Parser[U] {
  def parse(text:String): Try[Resultado[U]] = {//: Try[Resultado[List[T]]]
    for{
      result <- parser.parse(text)
    } yield result.copy(parsed = mapFunction(result.parsed))
  }
}



case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[List[Any]] {
  def parse(text:String): Try[Resultado[List[Any]]] = {//: Try[Resultado[List[T]]]
    for {
      uno <- ((parserContent <~ parserSep <> this) <|> parserContent).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

case object parserEspacios extends Parser[List[Any]] {
  def parse(text: String): Try[Resultado[List[Any]]] = {
    satisfies(anyChar())(_.isWhitespace).*.parse(text)
//    (char(' ') <|> char('\n')).*.parse(text)
  } //Claúsula de kleene con n cantidad de espacios
}

case class Punto(x: Double, y: Double){
  def position() ={
    (x,y)
  }
}

case object parserPunto extends Parser[Punto] {
  def parse(text: String): Try[Resultado[Punto]] = {
    val parserPartes = (double() <~ parserEspacios <~ string("@")) <> (parserEspacios ~> double())
    parserPartes.parse(text).map(resultado => resultado.parsed match {
      case List(a,b) => Resultado(Punto(a.asInstanceOf[Double],b.asInstanceOf[Double]), resultado.notParsed)
    })
  }
}

case class Cuadrado(topLeft: Punto, bottomRight: Punto){
  def daleeeeee_pa() ={
    (topLeft.position(),bottomRight.position())
  }

  def dale() = {
    topLeft.position()
  }

  def paaaaaa() = {
    bottomRight.position()
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
      case List(a,b) => Resultado(Cuadrado(a.asInstanceOf[Punto],b.asInstanceOf[Punto]), resultado.notParsed)
    })
  }
}

case object pruebitas extends App {
  println(parserCuadrado.parse("cuadrado[0 @ 100,200 @ 300]"))
  val cuadrado = parserCuadrado.parse("cuadrado[150 @ 100,200 @ 300]").get.parsed
  TADPDrawingAdapter
    .forScreen { adapter =>
      adapter
        .beginColor(Color.rgb(100, 100, 100))
        .rectangle(cuadrado.dale(), cuadrado.paaaaaa())
        .end()
    }
}