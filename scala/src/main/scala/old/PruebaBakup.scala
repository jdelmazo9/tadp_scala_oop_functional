//package tadp
//import scala.util.{Success, Try}
////TEMATICA: ???¿¿¿
////ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
////PARSER COMBINATOR (PARSER->PARSER->PARSER)
//
////case class parser.Parser(a: String) {
////  require(a < Micro.REGISTER_SIZE && a > -Micro.REGISTER_SIZE)
////  require(b < Micro.REGISTER_SIZE && b > -Micro.REGISTER_SIZE)
////  require(mem.size == Micro.MEM_SIZE)
////}
//
//
//case class tadp.Resultado(parsed: Any, notParsed: String) {
//}
//
//class tadp.ParserErrorException(val resultado: tadp.Resultado) extends RuntimeException
//
//case object main extends App {
//
//  def parser.anyChar: Function1[String, Any] = {
//    val f = (text: String) => Try(
//      text match {
//        case t if !t.isEmpty => tadp.Resultado(text.head, text.tail)
//        case _ => throw new tadp.ParserErrorException(tadp.Resultado(null, text))
//      }
//    )
//    f
//  }
//  // val f = (text: String) => Try(tadp.Resultado(text.head, text.tail))
//
//  def parser.char(character: Char): PartialFunction[String, Try[tadp.Resultado]] = {
//    val f: PartialFunction[String, Try[tadp.Resultado]] = {
//      case text if text.head == character => Try(tadp.Resultado(text.head, text.tail))
//    }
//    f
//        // (text: String) =>
////        print(text)
////        Try(
//////          text.head match {
////
////            //          case _ => throw new tadp.ParserErrorException(tadp.Resultado(null, text))
//////          }
////        )
//
////    f
//  }
//
//  def parser.digit: Function1[String, Any] = {
//    val f = (text: String) => if (text.head.isDigit) text.head
//    f
//  }
//
//  def parser.string(aString: String): Function1[String, Any] = {
//    val f = (text: String) => if (text.startsWith(aString)) aString
//    f
//  }
//
//  def parser.integer: Function1[String, Any] = {
//    val f = (text: String) => if ("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt
//    f
//  }
//
//  def parser.double: Function1[String, Any] = {
//    val f = (text: String) => if ("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble
//    f
//  }
//
//  // parser.orCombinator([])
//
////  def orCombinator2(p1:PartialFunction[String, Try[tadp.Resultado]], p2:PartialFunction[String, Try[tadp.Resultado]]): PartialFunction[String, Try[tadp.Resultado]] = {
////    val parsers: List[PartialFunction[String, Try[tadp.Resultado]]] = List(p1,p2)
////    val f = (resultado: tadp.Resultado) => {
////      parsers.foldLeft(Try(resultado)) { (previousResult, parser) =>
////        parser match {
////          case _ => for(res <- previousResult) yield micro.copy(a = micro.a + micro.b)
////        }
////      }
////    }
////  }
//
//    def parser.orCombinator(p1:PartialFunction[String, Try[tadp.Resultado]], p2:PartialFunction[String, Try[tadp.Resultado]]): PartialFunction[String, Try[tadp.Resultado]] = {
//    val f: PartialFunction[String, Try[tadp.Resultado]] = {
//      case text if p1(text).isSuccess => p1(text)
//      case text if p2(text).isSuccess => p2(text)
//      case text => Try(throw new tadp.ParserErrorException(tadp.Resultado(null,text)))
//    }
//    f
//  }
//
////  print("aaaaaaa")
////  print(parser.anyChar("hola"))
////  print(parser.anyChar(""))
////  val parser_c = parser.char('c')
////  val parser_h = parser.char('h')
//
////  val samples :List[String] = List("au")
////
////  val algo = samples map (parser.char('h') orElse parser.char('c'))
//  print(parser.orCombinator(parser.char('h'), parser.char('c'))("cau"))
//
//}
////  def parser.integer: Function1[String, Any] = {
////    val f: PartialFunction [String, Any] = {
////      case text if ("\\-?\\d+".r.pattern.matcher(text).matches()) => text.toInt
////    }
////    f
////  }
////
////  def parser.double: Function1[String, Any] = {
////    val f: PartialFunction [String, Any] = {
////      case text if ("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) => text.toDouble
////    }
////    f
////  }
//
////  val f: PartialFunction[(Char, String), Char] = {
////    case (caracter, cadena) if caracter == cadena.head => caracter
////  } ;
//
////  val concatenate = parser.integer compose parser.double
//
//
////  print(concatenate)
////
////  print(f.getClass);
