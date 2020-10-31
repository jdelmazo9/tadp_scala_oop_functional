//package tadp

import jdk.nashorn.internal.runtime.ParserException

import scala.collection.View.Empty
import scala.util.{Failure, Success, Try}
//package tadp

// char('c')("cha")
// char('h')("ha")
//

case object main extends App{

  // Tincho dice que es una instruccion
  def parse(parser: Parser, text: String): Try[Resultado] = {
    parser match {
      case char(character)              => Try(text match {
                                                      case _ if (text.head == character) => Resultado(text.head, text.tail)
                                                      case _ => throw new ParserErrorException(Resultado(null, text))
                                                    })

      case string(aString)              => Try(text match {
                                                      case _ if (text.startsWith(aString)) => Resultado(aString, text.substring(aString.length))
                                                      case _ => throw new ParserErrorException(Resultado(null, text))
                                                    })

      case integer()                    => Try("\\-?\\d+".r.findFirstMatchIn(text) match {
                                                      case Some(i) => Resultado(i.group(0), text.substring(i.group(0).length))
                                                      case _ => throw new ParserErrorException(Resultado(null, text))
                                                  })

      case orCombinator(p1, p2)         => if (parse(p1, text).isSuccess) parse(p1, text) else parse(p2, text)

      case concatCombinator(p1,p2)      => for {
                                              result1 <- parse(p1, text) // Resultado("hola", "mundo!")
                                              result2 <- parse(p2, result1.notParsed)
                                            } yield result2.copy(parsed = (result1.parsed, result2.parsed))

      case rightmostCombinator(p1,p2)   => for {
                                              result1 <- parse(p1, text) // Resultado("hola", "mundo!")
                                              result2 <- parse(p2, result1.notParsed)
                                            } yield result2

      case leftmostCombinator(p1,p2)    => for {
                                              result1 <- parse(p1, text) // Resultado("hola", "mundo!")
                                              _ <- parse(p2, result1.notParsed)
                                            } yield result1

//      case sepByCombinator(cont,sep)    => text.fold(Try(throw new ParserErrorException(Resultado(null, text) )) { (t,r) =>
//
//
//      }



//        text match {
//        case _ if text.isEmpty => Try(throw new ParserErrorException(Resultado(null, text)))
//        case _ => for { //123-456
//                result1 <- parse(cont, text) //Resultado(123, -456)
////              result2 <- parse(sep, result1.notParsed) if !result1.notParsed.isEmpty //Resultado(-, 456)
////              result3 <- parse(cont, result2.notParsed) //Resultado(-, 456)
////              if !result1.notParsed.isEmpty
////              result3 <- parse(sepByCombinator(sep, cont), result1.notParsed)
//                result2 <- parse(sepByCombinator(sep, cont), result1.notParsed)
//              } yield if(result2.notParsed.isEmpty) result2 else result1
////              } yield if(result1.notParsed.isEmpty) result1 else result2
//      }
    }
  }

  println(parse(integer().sepBy(char('-')), "1235-1"))
//  println(parse(char('c'), "cola"))
  println(parse(integer(), "cola"))
  println(parse(integer(), "1234"))
  println(parse(integer(), "1234asdfgh"))
//  println(parse(orCombinator(char('c'), char('h')), "cola"))
//  println(parse(orCombinator(char('c'), char('h')), "hola"))
  println(parse(orCombinator(char('c'), char('o')), "ola"))
  println(parse(char('c') <|> char('o'), "ola"))
//  println(parse(string("hola"), "holamundo!"))
  println(parse(concatCombinator(string("hola"), string("mundo")), "holamundo!"))
  println(parse(string("hola") <> string("mundo"), "holamundo!"))
//  println(parse(concatCombinator(char('h'), char('o')), "ola"))
//  println(parse(concatCombinator(char('h'), char('o')), "hla"))

}