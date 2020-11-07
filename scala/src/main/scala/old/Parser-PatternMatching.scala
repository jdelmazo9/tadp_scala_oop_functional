////package tadp
//
//import jdk.nashorn.internal.runtime.ParserException
//
//import scala.collection.View.Empty
//import scala.util.{Failure, Success, Try}
////package tadp
//
//// char('c')("cha")
//// char('h')("ha")
////
//
//case object main extends App{
//
//  def parse(parser: Parser, text: String): Try[tadp.Resultado] = {
//    parser match {
//      case char(character)              => Try(text match {
//                                                      case _ if (text.head == character) => tadp.Resultado(text.head, text.tail)
//                                                      case _ => throw new tadp.ParserErrorException(tadp.Resultado(null, text))
//                                                    })
//
//      case string(aString)              => Try(text match {
//                                                      case _ if (text.startsWith(aString)) => tadp.Resultado(aString, text.substring(aString.length))
//                                                      case _ => throw new tadp.ParserErrorException(tadp.Resultado(null, text))
//                                                    })
//
//      case integer()                    => Try("\\-?\\d+".r.findFirstMatchIn(text) match {
//                                                      case Some(i) => tadp.Resultado(i.group(0), text.substring(i.group(0).length))
//                                                      case _ => throw new tadp.ParserErrorException(tadp.Resultado(null, text))
//                                                  })
//
//      case orCombinator(p1, p2)         => if (parse(p1, text).isSuccess) parse(p1, text) else parse(p2, text)
//
//      case concatCombinator(p1,p2)      => for {
//                                              result1 <- parse(p1, text) // tadp.Resultado("hola", "mundo!")
//                                              result2 <- parse(p2, result1.notParsed)
//                                            } yield result2.copy(parsed = (result1.parsed, result2.parsed))
//
//      case rightmostCombinator(p1,p2)   => for {
//                                              result1 <- parse(p1, text) // tadp.Resultado("hola", "mundo!")
//                                              result2 <- parse(p2, result1.notParsed)
//                                            } yield result2
//
//      case leftmostCombinator(p1,p2)    => for {
//                                              result1 <- parse(p1, text) // tadp.Resultado("hola", "mundo!")
//                                              result2 <- parse(p2, result1.notParsed)
//                                            } yield result1.copy(notParsed = result2.notParsed)
//
////      case sepByCombinator(cont,sep)   => { for {
////                                              result1 <- parse(cont<>sep, text)
////                                              result2 <- parse(cont, text)
////                                              next    <- parse(sepByCombinator(cont, sep), text)
//////                                              result1 <- parse(cont<>sep, text)
//////                                              result2 <- parse(cont<>sep, result1.notParsed)
//////                                              result3 <- parse(cont<>sep, result2.notParsed)
//////                                              result4 <- parse(cont<>sep, result3.notParsed)
//////                                              result5 <- parse(cont<>sep, result4.notParsed)
//////                                              result6 <- parse(cont, result5.notParsed)
////                                            } yield result1
////
////      }
////        val recursive: Parser => Parser => tadp.Resultado => Try[tadp.Resultado] =
////          cont => {
////            sep => {
////              prevResult => {
////                val r = parse(cont, text)
////                if(r.isSuccess){
////                  return recursive(sep,cont,prevResult.copy())
////                }
////              }
////            }
////          }
////        val r1 = parse(cont, text)
////        if(r1.isSuccess) {
////          val r2 = parse(sep, r1.get.notParsed)
////          if(r2.isSuccess){
////            return parse(sepByCombinator(cont,sep),r2.get.notParsed)
////          }
////          else{
////            return r1
////          }
////        }
////        Try(throw new tadp.ParserErrorException(tadp.Resultado(null, text)))
//////        val r1 = parse(cont, text)
////      }
//
//
//
////        text match {
////        case _ if text.isEmpty => Try(throw new tadp.ParserErrorException(tadp.Resultado(null, text)))
////        case _ => for { //123-456
////                result1 <- parse(cont, text) //tadp.Resultado(123, -456)
//////              result2 <- parse(sep, result1.notParsed) if !result1.notParsed.isEmpty //tadp.Resultado(-, 456)
//////              result3 <- parse(cont, result2.notParsed) //tadp.Resultado(-, 456)
//////              if !result1.notParsed.isEmpty
//////              result3 <- parse(sepByCombinator(sep, cont), result1.notParsed)
////                result2 <- parse(sepByCombinator(sep, cont), result1.notParsed)
////              } yield if(result2.notParsed.isEmpty) result2 else result1
//////              } yield if(result1.notParsed.isEmpty) result1 else result2
////      }
//    }
//  }
//
////  println(parse(integer().sepBy(char('-')), "1235-"))
////  println(parse(char('c'), "cola"))
//  println(parse(integer(), "cola"))
//  println(parse(integer(), "1234"))
//  println(parse(integer(), "1234asdfgh"))
//
//
//  println(parse(orCombinator(char('c'), char('h')), "cola"))
//  println(parse(orCombinator(char('c'), char('h')), "hola"))
//
//
//  println(parse(orCombinator(char('c'), char('o')), "ola"))
//  println(parse(char('c') <|> char('o'), "ola"))
////  println(parse(string("hola"), "holamundo!"))
//  println(parse(concatCombinator(string("hola"), string("mundo")), "holamundo!"))
//  println(parse(string("hola") <> string("mundo"), "holamundo!"))
//  println(parse(string("hola") <~ string("mundo"), "holamundo!"))
//  println(parse(string("hola") ~> string("mundo"), "holamundo!"))
//  println(parse(string("hola") ~> string("mundo"), "holamndo!"))
////  println(parse(concatCombinator(char('h'), char('o')), "ola"))
////  println(parse(concatCombinator(char('h'), char('o')), "hla"))
//
//}