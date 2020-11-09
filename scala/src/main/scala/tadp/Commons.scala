package tadp

case class Resultado[+T](parsed: T, notParsed: String)
class ParserErrorException[T](val resultado: Resultado[T]) extends RuntimeException

case object utilities {
  def aplanandoAndoBackup[T](algoAplanableONoTanto: Any): List[T] =
    algoAplanableONoTanto match {
      case None => List().asInstanceOf[List[T]]
      case (a, b) => (aplanandoAndo(a) ++ aplanandoAndo(b)).asInstanceOf[List[T]]
      case List(a :: b) => (a :: aplanandoAndo(b)).asInstanceOf[List[T]]
      case a :: b => (a :: aplanandoAndo(b)).asInstanceOf[List[T]]
      case a if a != Nil => List(a).asInstanceOf[List[T]]
      case _ => List().asInstanceOf[List[T]]
    }

  def aplanandoAndo[T](algoAplanableONoTanto: Any): List[T] =
    algoAplanableONoTanto match {
      case None => List().asInstanceOf[List[T]]
//      case (a: List[Any], b) => (a :+ b).asInstanceOf[List[T]]
      case (a, b) if a.isInstanceOf[List[Any]] && b.isInstanceOf[List[Any]] => (a.asInstanceOf[List[Any]] :+ b.asInstanceOf[List[Any]]).asInstanceOf[List[T]]
      case (a, b) if a.isInstanceOf[List[Any]] => (a.asInstanceOf[List[Any]] :+ b).asInstanceOf[List[T]]
      case (a, b) if b.isInstanceOf[List[Any]] => (a :: b.asInstanceOf[List[Any]]).asInstanceOf[List[T]]
      case (a, b) => List(a,b).asInstanceOf[List[T]]
//      case (a, b) => (aplanandoAndo(a) ++ aplanandoAndo(b)).asInstanceOf[List[T]]
      case List(a :: b) => (a :: b).asInstanceOf[List[T]]
      case a :: b => (a :: b).asInstanceOf[List[T]]
      case a if a != Nil => List(a).asInstanceOf[List[T]]
      case _ => List().asInstanceOf[List[T]]
    }
}

case class Punto(x: Double, y: Double){
  def position(): (Double, Double) ={
    (x,y)
  }
}

case object pruebaAplanadora extends App{
  val a = utilities.aplanandoAndo[Punto](List(Punto(1,2)))
}