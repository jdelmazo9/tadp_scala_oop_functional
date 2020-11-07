case class Resultado[+T](parsed: T, notParsed: String)
class ParserErrorException[T](val resultado: Resultado[T]) extends RuntimeException

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
}