//TEMATICA: ???¿¿¿
//ENTIDADES QUE NO DUDAMOS QUE TIENEN QUE ESTAR: PARSER{<TEXTO = {ARBOL}>} (estructura facil de leer)
//PARSER COMBINATOR (PARSER->PARSER->PARSER)

//case class Parser(a: String) {
//  require(a < Micro.REGISTER_SIZE && a > -Micro.REGISTER_SIZE)
//  require(b < Micro.REGISTER_SIZE && b > -Micro.REGISTER_SIZE)
//  require(mem.size == Micro.MEM_SIZE)
//}

case object main extends App {
  def anyChar: Function1[String, Any] = {
    val f = (text: String) => text.head
    f
  }

  def char(character: Char): Function1[String, Any] = {
    val f = (text: String) => if(text.head == character) text.head
    f
  }

  def digit: Function1[String, Any] = {
    val f = (text: String) => if(text.head.isDigit) text.head
    f
  }

  def string(aString: String): Function1[String, Any] = {
    val f = (text: String) => if(text.startsWith(aString)) aString
    f
  }

//  def integer: Function1[String, Any] = {
//    val f = (text: String) => if("\\-?\\d+".r.pattern.matcher(text).matches()) text.toInt
//    f
//  }
//
//  def double: Function1[String, Any] = {
//    val f = (text: String) => if("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) text.toDouble
//    f
//  }

  def integer: Function1[String, Any] = {
    val f: PartialFunction [String, Any] = {
      case text if ("\\-?\\d+".r.pattern.matcher(text).matches()) => text.toInt
    }
    f
  }

  def double: Function1[String, Any] = {
    val f: PartialFunction [String, Any] = {
      case text if ("\\-?\\d+(\\.\\d+)?".r.pattern.matcher(text).matches()) => text.toDouble
    }
    f
  }

  val f: PartialFunction[(Char, String), Char] = {
    case (caracter, cadena) if caracter == cadena.head => caracter
  } ;

//  val concatenate = integer compose double



//  print(concatenate)
//
//  print(f.getClass);
}