//import scala.util.Try
////////TEMATICA: ???¿¿¿
////////ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
////////PARSER COMBINATOR (PARSER->PARSER->PARSER)
//////
////case class Resultado(parsed: Any, notParsed: String)
//////
////class ParserErrorException(val resultado: Resultado) extends RuntimeException
//////
//////
//
////case class Parser(parsed: Any, notParsed: String, success: Boolean){
////  def <|>(other: Parser): Parser =
////    if(this.success) this.copy() else other.copy()
////}
////
////case object char extends Parser{
////
////}
////
////implicit class Parser2(parser: Parser) {
////  def <|>(other: Parser) = (parser, other)
////}
////
////2.isAFactorOf(10)
////// or, without dot-syntax
////2 isAFactorOf 10
//
//case object tuviejamain extends App {
////
//  type Parser[T] = String => Try[Resultado[T]]
////
////  val anyChar: Parser = text => Try(Resultado(text.head, text.tail))
////
//  val char: Char => Parser =
//    character => text => Try(text match {
//      case _ if (text.head == character) => Resultado(text.head,text.tail)
//      case _ => throw new ParserErrorException(Resultado(null,text))
//    })
//
////  class Parser1(val p: Parser) {
////    def <|>(otherParser: Parser): Parser ={
////
////    }
////  }
////  val p1 <|> p2 => Parser => Parser
////
////  val digit: Parser = text => Try(if(text.head.isDigit) text.head)
////
////  val string: String => Parser = aString => text => Try(if(text.startsWith(aString)) aString)
////
////  val integer: Parser = text => Try(if("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt)
////
////  val double: Parser = text => Try(if("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble)
////
////  //Tests ahre
////  println(anyChar("hola"))
//  println(char('h')("hola"))
//  println(char('h')("cola"))
////  println(digit("2"))
////  println(string("hola")("hola mundo"))
////  println(integer("-82389"))
////  println(double("0.7328"))
////
//}
//
