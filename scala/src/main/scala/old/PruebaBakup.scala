//package tadp
//import scala.util.{Success, Try}
////TEMATICA: ???¿¿¿
////ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
////PARSER COMBINATOR (PARSER->PARSER->PARSER)
//
////case class Parser(a: String) {
////  require(a < Micro.REGISTER_SIZE && a > -Micro.REGISTER_SIZE)
////  require(b < Micro.REGISTER_SIZE && b > -Micro.REGISTER_SIZE)
////  require(mem.size == Micro.MEM_SIZE)
////}
//
//
//case class Resultado(parsed: Any, notParsed: String) {
//}
//
//class ParserErrorException(val resultado: Resultado) extends RuntimeException
//
//case object main extends App {
//
//  def anyChar: Function1[String, Any] = {
//    val f = (text: String) => Try(
//      text match {
//        case t if !t.isEmpty => Resultado(text.head, text.tail)
//        case _ => throw new ParserErrorException(Resultado(null, text))
//      }
//    )
//    f
//  }
//  // val f = (text: String) => Try(Resultado(text.head, text.tail))
//
//  def char(character: Char): PartialFunction[String, Try[Resultado]] = {
//    val f: PartialFunction[String, Try[Resultado]] = {
//      case text if text.head == character => Try(Resultado(text.head, text.tail))
//    }
//    f
//        // (text: String) =>
////        print(text)
////        Try(
//////          text.head match {
////
////            //          case _ => throw new ParserErrorException(Resultado(null, text))
//////          }
////        )
//
////    f
//  }
//
//  def digit: Function1[String, Any] = {
//    val f = (text: String) => if (text.head.isDigit) text.head
//    f
//  }
//
//  def string(aString: String): Function1[String, Any] = {
//    val f = (text: String) => if (text.startsWith(aString)) aString
//    f
//  }
//
//  def integer: Function1[String, Any] = {
//    val f = (text: String) => if ("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt
//    f
//  }
//
//  def double: Function1[String, Any] = {
//    val f = (text: String) => if ("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble
//    f
//  }
//
//  // orCombinator([])
//
////  def orCombinator2(p1:PartialFunction[String, Try[Resultado]], p2:PartialFunction[String, Try[Resultado]]): PartialFunction[String, Try[Resultado]] = {
////    val parsers: List[PartialFunction[String, Try[Resultado]]] = List(p1,p2)
////    val f = (resultado: Resultado) => {
////      parsers.foldLeft(Try(resultado)) { (previousResult, parser) =>
////        parser match {
////          case _ => for(res <- previousResult) yield micro.copy(a = micro.a + micro.b)
////        }
////      }
////    }
////  }
//
//    def orCombinator(p1:PartialFunction[String, Try[Resultado]], p2:PartialFunction[String, Try[Resultado]]): PartialFunction[String, Try[Resultado]] = {
//    val f: PartialFunction[String, Try[Resultado]] = {
//      case text if p1(text).isSuccess => p1(text)
//      case text if p2(text).isSuccess => p2(text)
//      case text => Try(throw new ParserErrorException(Resultado(null,text)))
//    }
//    f
//  }
//
////  print("aaaaaaa")
////  print(anyChar("hola"))
////  print(anyChar(""))
////  val parser_c = char('c')
////  val parser_h = char('h')
//
////  val samples :List[String] = List("au")
////
////  val algo = samples map (char('h') orElse char('c'))
//  print(orCombinator(char('h'), char('c'))("cau"))
//
//}
////  def integer: Function1[String, Any] = {
////    val f: PartialFunction [String, Any] = {
////      case text if ("\\-?\\d+".r.pattern.matcher(text).matches()) => text.toInt
////    }
////    f
////  }
////
////  def double: Function1[String, Any] = {
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
////  val concatenate = integer compose double
//
//
////  print(concatenate)
////
////  print(f.getClass);
