import scala.runtime.Nothing$
import scala.util.{Failure, Success, Try}

//println(utilities.aplanandoAndo(List(1,2)))

case object utilities {
  val aplanandoAndo: Any => List[Any] = {
    case None => List()
    case (a,b) => a :: aplanandoAndo(b)
    case List(a::b) => a :: aplanandoAndo(b)
    case a::b => a :: aplanandoAndo(b)
    case a if a != Nil => List(a)
    case _ => List()
  }
  val tuplaDe: (Any, Any) => (Any) = {
    case (a,None) => (a)
    case (None, b) => (b)
    case (a, List(None)) => (a)
    case (List(None), b) => (b)
    case loquevenga => (loquevenga)
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

  def <>[U](other: Parser[U]): Parser[(Any)] ={
    concatCombinator(this, other)
  }

  def ~>[U](other: Parser[U]): rightmostCombinator[T, U] ={
    rightmostCombinator(this, other)
  }

  def <~[U](other: Parser[U]): leftmostCombinator[T, U] ={
    leftmostCombinator(this, other)
  }

//  def sepBy(sep: Parser[U]): sepByCombinator[T, Any] ={
//    sepByCombinator(this, sep)
//  }

  def parse(text: String): Try[Resultado[T]]
}

case class anyChar() extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    Try(text match {
      case _ if !text.isEmpty => Resultado(text.head, text.tail)
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

case class concatCombinator[+T,+U](parser1: Parser[T], parser2: Parser[U]) extends Parser[(Any)] {
  def parse(text:String): Try[Resultado[(Any)]] = { //Try[Resultado[(T,U)]] = {
    for {
      result1 <- parser1.parse(text) // Resultado("hola", "mundo!")
      result2 <- parser2.parse(result1.notParsed)
    } yield result2.copy(parsed = utilities.tuplaDe(result1.parsed, result2.parsed))
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
case class satisfies[+T](parser: Parser[T], condition: Any => Boolean) extends Parser[T] {
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
case class clausuraDeKleene[+T >: Null](parser: Parser[T]) extends Parser[List[Any]] {
  def parse(text:String): Try[Resultado[List[Any]]] = {
    for {
      uno <- opt(parser <> this).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

//  +: es como la clausura de Kleene pero requiere que el parser se aplique al menos UNA vez.
case class clausuraDeKleenePositiva[+T >: Null](parser: Parser[T]) extends Parser[List[Any]] {
  def parse(text:String): Try[Resultado[List[Any]]] = {
    for {
      uno <- (parser <> clausuraDeKleene(parser)).parse(text)
    } yield uno.copy(parsed = utilities.aplanandoAndo(uno.parsed))
  }
}

case class map[T, U](parser: Parser[T], mapFunction: T => U) extends Parser[U] {
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

//case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[Any] {
//  def parse(text:String): Try[Resultado[Any]] = {
//    for {
//      result <- ((parserContent <~ parserSep) <|> parserContent).parse(text)
//    } yield result.copy(parsed=List(result.parsed),result.notParsed)
//  }
//}
// sepByCombinator(integer(),char('-')).parse("123-456")
// integer <~ char(-) (123-456) => parsea 123, y deja en el Result(123,456) en el result.notParsed
// char(-) (123-456) => xFalla, devuelve el anterior


//case class sepByCombinator[+T,+U](parserContent: Parser[T], parserSep: Parser[U]) extends Parser[Any] {
//  def parse(text:String): Try[Resultado[Any]] = {
//    for {
//      result1 <- parserContent.parse(text)
//      result2 <- parserSep.parse(result1.notParsed)
//      } yield result1.copy(parsed = List(result1.parsed, result2.notParsed), "")// notParsed=result2.notParsed
//  }
//}

//implicit class Parser2(parser: Parser) {
//  def <|>(other: Parser) = (parser, other)
//}



case object pruebitas extends App {

//  def isInt(algo: Any): Boolean = {
//    algo.getClass.isInstanceOf[Int]
//  }

  val isString: Any => Boolean = {
    algo => algo.isInstanceOf[String]//algo.getClass.equals("String".getClass)
  }


//  println("Tests de Parsers individuales")
//    println("\tChar: ")
//      print("\t\t1. "); println(char('c').parse("chau")) //Success
//      print("\t\t2. "); println(char('c').parse("hau"))  //Failure
//      print("\t\t3. "); println(char('c').parse(""))  //Failure
//    println("\tAnyChar: ")
//      print("\t\t1. "); println(anyChar().parse("hola")) //Success
//      print("\t\t2. "); println(anyChar().parse("")) //Failure
//    println("\tDigit: ")
//      print("\t\t1. "); println(digit().parse("12asdf")) //Success
//      print("\t\t2. "); println(digit().parse("a123")) //Failure
//      print("\t\t3. "); println(digit().parse("327589")) //Success
//      print("\t\t4. "); println(digit().parse("4")) //Success
//    println("\tString: ")
//      print("\t\t1. "); println(string("hola").parse("hola mundo!")) //Success
//      print("\t\t2. "); println(string("hola").parse("holgado mundo!")) //Failure
//    println("\tInteger: ")
//      print("\t\t1. "); println(integer().parse("-")) //Failure
//      print("\t\t1. "); println(integer().parse("hau")) //Failure
//      print("\t\t2. "); println(integer().parse("123.asd")) //Success
//      print("\t\t3. "); println(integer().parse("123-456")) //Success
//      print("\t\t4. "); println(integer().parse("-43534543")) //Success
//    println("\tDouble: ")
//      print("\t\t1. "); println(double().parse("hau")) //Failure
//      print("\t\t2. "); println(double().parse("123.asd")) //Success
//      print("\t\t3. "); println(double().parse("-4353.4543")) //Success
//  println("")
//  println("Tests de Parsers Combinators")
//    println("\tOR combinator: ")
//      print("\t\t1. "); println((char('c') <|> char('o')).parse("cola")) //Parsea con el primero
//      print("\t\t2. "); println((char('c') <|> char('h')).parse("hau")) //Parsea con el segundo
//      print("\t\t3. "); println(orCombinator(char('c'), char('h')).parse("hau")) //Parsea con el segundo
//      print("\t\t4. "); println(orCombinator(char('c'), char('h')).parse("cau")) //Parsea con el primero
//      print("\t\t5. "); println(orCombinator(char('c'), char('h')).parse("au")) //Falla
  println("\tConcat combinator: ")
      print("\t\t1. "); println((string("hola") <> string("mundo")).parse("holamundo")) //Parsea el primero y el segundo
      print("\t\t1. "); println((string("hola") <> string("chau")).parse("holamundo")) //Falla: Parsea el primero y no el segundo
      print("\t\t1. "); println((string("caca") <> string("mundo")).parse("holamundo")) //Falla: No parsea el primero
      print("\t\t1. "); println((opt(string("caca")) <> string("hola")).parse("holamundo")) //Falla: No parsea el primero

  //  println("\tRightmost combinator: ")
//    print("\t\t1. "); println((string("hola") ~> string("mundo")).parse("holamundo")) //Funciona, devuelve el de la derecha
//    print("\t\t2. "); println((string("caca") ~> string("mundo")).parse("holamundo")) //Falla: no parsea el de la izquierda
//    print("\t\t3. "); println((string("hola") ~> string("mudo")).parse("holamundo")) //Falla: no parsea el de la derecha
//  println("\tLeftmost combinator: ")
//    print("\t\t1. "); println((string("hola") <~ string("mundo")).parse("holamundo")) //Funciona, devuelve el de la derecha
//    print("\t\t2. "); println((string("caca") <~ string("mundo")).parse("holamundo")) //Falla: no parsea el de la izquierda
//    print("\t\t3. "); println((string("hola") <~ string("mudo")).parse("holamundo")) //Falla: no parsea el de la derecha
//  println("\tSeparated-by combinator: ")
//    print("\t\t1. "); println(sepByCombinator(integer(),char('-')).parse("123-abc"))
//    print("\t\t2. "); println(sepByCombinator(integer(),char('-')).parse(""))
//    print("\t\t3. "); println(sepByCombinator(integer(),char('-')).parse("123"))
//    print("\t\t4. "); println(sepByCombinator(integer(),char('-')).parse("123-456"))
//    print("\t\t5. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789"))
//    print("\t\t6. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-000"))
//    print("\t\t7. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-aaa"))
//    print("\t\t8. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-"))
//    print("\t\t9. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789--"))
//    print("\t\t10. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95"))
//    print("\t\t10. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95---1--2--0-0-0-0-0-0-"))
//  println("\tSatisfies combinator: ")
//  print("\t\t1. "); println(satisfies(string("hola"), isString).parse("hola mundo!"))
//  print("\t\t2. "); println(satisfies(anyChar(), isString).parse("hola mundo!"))
//  print("\t\t3. "); println(satisfies(string("hola"), isString).parse("hol mundo!"))
//  println("\tOpt combinator: ")
//  print("\t\t1. "); println(opt(satisfies(string("hola"), isString)).parse("hola mundo!"))
//  print("\t\t2. "); println(opt(satisfies(anyChar(), isString)).parse("hola mundo!"))
//  println("\tMap combinator: ")
//  print("\t\t1. "); println(map(integer(), (x:Int) => x * x).parse("12 mundo!"))
//  print("\t\t2. "); println(map(integer(), (x:Int) => x * x).parse("hola mundo!"))
  println("\tClausura de Kleene combinator: ")
  print("\t\t1. "); println(clausuraDeKleene(anyChar()).parse("12 mundo!"))
  print("\t\t2. "); println(clausuraDeKleene(string("hola")).parse("holaholahola mundo!"))
  print("\t\t3. "); println(clausuraDeKleene(string("hola")).parse("chau mundo!"))
  println("\tClausura de Kleene Positiva combinator: ")
  print("\t\t1. "); println(clausuraDeKleenePositiva(anyChar()).parse("12 mundo!"))
  print("\t\t2. "); println(clausuraDeKleenePositiva(string("hola")).parse("holaholahola mundo!"))
  print("\t\t3. "); println(clausuraDeKleenePositiva(string("hola")).parse("chau mundo!"))


//  case class Persona(nombre: String, apellido: String)
//  val personaParser = (alphaNum.* <> (char(' ') ~> alphaNum.*))
//    .map { case (nombre, apellido) => Persona(nombre, apellido) }


  //  print(((23, "foo"), (), (true, 2.0)) flatMap identity)
//  println(utilities.aplanandoAndo((1,(2,3))))
//  println(utilities.aplanandoAndo((1,(2,(2,(2,(2,3)))))))

//  println(f.productIterator.)
//  f.productIterator.flatMap(identity(f))
//  f.productIterator.foldLeft(List(), v => v.)
  //  type Magic[+A, +B] = Either[A, B]
//  val magic: Magic[String,Int] = "Try(Resultado)"
//
////  val l: Either[String, Int] = "String"
//  val l: Either[String, Int] = "a"
}
//case object Digit extends Parser