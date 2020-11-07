case object utilities2 extends App{
  val aplanandoAndo2: Any => List[Any] = {
    case (a,b) => a :: aplanandoAndo2(b)
    case List(a::b) => a :: aplanandoAndo2(b)
    case a::b => a :: aplanandoAndo2(b)
    case a if a != Nil => List(a)
    case _ => List()
    //    case a::b =>
  }

  val tuplaDe: (Any, Any) => (Any) = {
    case (a,None) => (a)
    case (None, b) => (b)
    case loquevenga => (loquevenga)
  }

//  println(aplanandoAndo2(List(1,2)))
  println(aplanandoAndo2(List(1,2,3)))
  println(aplanandoAndo2(List(1,List(1,2))))
//  println(aplanandoAndo2((1,(1,(1,(1,(1,(1,2))))))))
//  println(aplanandoAndo2(List(1,List(2,List(3,List(2,List(2,List(2,3))))))))
//
//  val f = List(1,List(2,3))
//  val a::b::c = f
//  println(a)
//  println(b)
  //List(1,   List(2,3)) )
  //     a        b

  //          List(2,3)
  //               a b
  //
}