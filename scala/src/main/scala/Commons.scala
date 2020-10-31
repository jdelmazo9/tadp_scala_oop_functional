
case class Resultado(parsed: Any, notParsed: String)
class ParserErrorException(val resultado: Resultado) extends RuntimeException