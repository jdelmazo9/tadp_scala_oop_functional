//123asdfgdigit-
case class Resultado[+T](parsed: T, notParsed: String)
class ParserErrorException[T](val resultado: Resultado[T]) extends RuntimeException