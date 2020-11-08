//import scala.util.Try
////////TEMATICA: ???¿¿¿
////////ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
////////PARSER COMBINATOR (PARSER->PARSER->PARSER)
//////
////case class tadp.Resultado(parsed: Any, notParsed: String)
//////
////class tadp.ParserErrorException(val resultado: tadp.Resultado) extends RuntimeException
//////
//////
//
////case class parser.Parser(parsed: Any, notParsed: String, success: Boolean){
////  def <|>(other: parser.Parser): parser.Parser =
////    if(this.success) this.copy() else other.copy()
////}
////
////case object parser.char extends parser.Parser{
////
////}
////
////implicit class Parser2(parser: parser.Parser) {
////  def <|>(other: parser.Parser) = (parser, other)
////}
////
////2.isAFactorOf(10)
////// or, without dot-syntax
////2 isAFactorOf 10
//
//case object tuviejamain extends App {
////
//  type parser.Parser[T] = String => Try[tadp.Resultado[T]]
////
//  val parser.anyChar: parser.Parser = text => Try(tadp.Resultado(text.head, text.tail))
////
//  val parser.char: Char => parser.Parser =
//    character => text => Try(text match {
//      case _ if (text.head == character) => tadp.Resultado(text.head,text.tail)
//      case _ => throw new tadp.ParserErrorException(tadp.Resultado(null,text))
//    })
//
////  class Parser1(val p: parser.Parser) {
////    def <|>(otherParser: parser.Parser): parser.Parser ={
////
////    }
////  }
////  val p1 <|> p2 => parser.Parser => parser.Parser
////
////  val parser.digit: parser.Parser = text => Try(if(text.head.isDigit) text.head)
////
////  val parser.string: String => parser.Parser = aString => text => Try(if(text.startsWith(aString)) aString)
////
////  val parser.integer: parser.Parser = text => Try(if("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt)
////
////  val parser.double: parser.Parser = text => Try(if("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble)
////
////  //Tests ahre
////  println(parser.anyChar("hola"))
//  println(parser.char('h')("hola"))
//  println(parser.char('h')("cola"))
////  println(parser.digit("2"))
////  println(parser.string("hola")("hola mundo"))
////  println(parser.integer("-82389"))
////  println(parser.double("0.7328"))
////
//}
//
