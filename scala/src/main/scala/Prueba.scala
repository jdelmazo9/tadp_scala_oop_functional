import scala.util.Try
//TEMATICA: ???¿¿¿
//ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
//PARSER COMBINATOR (PARSER->PARSER->PARSER)

case class Resultado(parsed: Any, notParsed: String)

class ParserErrorException(val resultado: Resultado) extends RuntimeException


case object main extends App {

  type Parser = String => Try[Resultado]

  val anyChar: Parser = text => Try(Resultado(text.head, text.tail))

  val char: Char => Parser =
    character => text => Try(text match {
      case _ if (text.head == character) => Resultado(text.head,text.tail)
      case _ => throw new ParserErrorException(Resultado(null,text))
    })

  val digit: Parser = text => Try(if(text.head.isDigit) text.head)

  val string: String => Parser = aString => text => Try(if(text.startsWith(aString)) aString)

  val integer: Parser = text => Try(if("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt)

  val double: Parser = text => Try(if("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble)

  //Tests ahre
  println(anyChar("hola"))
  println(char('h')("hola"))
  println(char('h')("cola"))
  println(digit("2"))
  println(string("hola")("hola mundo"))
  println(integer("-82389"))
  println(double("0.7328"))

}

