package parser
import java.time.LocalDateTime
import java.util.Dictionary

import Dibujante.{dibujar, simplificar}
import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter
import tadp.{Punto, Resultado, contador}

import scala.util.Try
import tree.{Circulo, Colorete, Cuadrado, Escala, For, Grupo, Nodo, Rotacion, Traslacion, Triangulo}

import scala.io.Source


// Objetivo poder parsear las instrucciones y armar un ast
//
// ejemplos
//    triangulo[0 @ 100, 200 @ 300, 150 @ 500] => Triangulo
//    rectangulo[0 @ 100, 200 @ 300]

case object parserEspacios extends Parser[List[Char]] {
  def parse(text: String): Try[Resultado[List[Char]]] = {
    //    (parser.satisfiesCombinator(parser.anyChar())(_.isWhitespace)).*.parse(text)
    (char('\r') <|> char(' ') <|> char('\n') <|> char('\t')).*.parse(text)
  }
}

case object parserPunto extends Parser[Punto] {
  def parse(text: String): Try[Resultado[Punto]] = {
    val parserPartes = (double() <~ parserEspacios <~ string("@")) <> (parserEspacios ~> double())
    parserPartes.parse(text).map(resultado => resultado.parsed match {
      case List(a,b) => Resultado(Punto(a,b), resultado.notParsed)
    })
  }
}

case object parserSep extends Parser[Char] {
  def parse(text: String): Try[Resultado[Char]] = {
    ((parserEspacios ~> char(',')) <~ parserEspacios).parse(text)
  }
}

case class parserParametros[+T,+U](parserContent: Parser[T], parserSep: Parser[U], limit: Integer) extends Parser[List[T]]{
  def parse(text: String): Try[Resultado[List[T]]] = {
    ((char('[') ~> parserEspacios ~> satisfiesCombinator(sepByCombinator(parserContent, parserSep))(_.length == limit)) <~ parserEspacios <~ char(']')).parse(text)
  }

}


case class parserHijo() extends Parser[Nodo]{
  def parse(text: String): Try[Resultado[Nodo]] = {
    (char('(') ~> parserEspacios ~> parserNodo <~ parserEspacios <~ char(')')).parse(text)
  }
}

case class parserHijos() extends Parser[List[Nodo]]{
  def parse(text: String): Try[Resultado[List[Nodo]]] = {
    (char('(') ~> parserEspacios ~> sepByCombinator(parserNodo, parserSep) <~ parserEspacios <~ char(')')).parse(text)
  }
}

case object parserRectangulo extends Parser[Cuadrado] {
  def parse(text: String): Try[Resultado[Cuadrado]] = {
    for {
      elemento <- string("rectangulo").parse(text)
      resultado <- parserParametros(parserPunto, parserSep, 2).parse(elemento.notParsed)
    } yield Resultado(Cuadrado(resultado.parsed.head, resultado.parsed.tail.head), resultado.notParsed)
  }
}

case object parserTriangulo extends Parser[Triangulo] {
  def parse(text: String): Try[Resultado[Triangulo]] = {
    for {
      elemento <- string("triangulo").parse(text)
      resultado <- parserParametros(parserPunto, parserSep, 3).parse(elemento.notParsed)
    } yield Resultado(Triangulo(resultado.parsed.head, resultado.parsed(1), resultado.parsed(2)), resultado.notParsed)
  }
}

case object parserCirculo extends Parser[Circulo] {
  def parse(text: String): Try[Resultado[Circulo]] = {
    for {
      elemento <- string("circulo").parse(text)
      resultadoPrimerParametro <- (char('[') ~> parserEspacios ~> ( parserPunto <~ parserSep )).parse(elemento.notParsed)
      resultadoSegundoParametro <-  (double() <~ parserEspacios <~ char(']')).parse(resultadoPrimerParametro.notParsed)
    } yield Resultado(Circulo(resultadoPrimerParametro.parsed, resultadoSegundoParametro.parsed), resultadoSegundoParametro.notParsed)
  }
}

// resultado( parsed = List(Color, Grupo( Rectangulo(...), Rectangulo(...), Rectangulo(...) ) )
case object parserColor extends Parser[Colorete] {
  def parse(text: String): Try[Resultado[Colorete]] = {
    for {
      elemento <- string("color").parse(text)
      resultadoParametros <- (parserParametros(integer(), parserSep, 3)).parse(elemento.notParsed)
      resultadoHijo <- parserHijo().parse(resultadoParametros.notParsed)
    } yield Resultado(Colorete(resultadoHijo.parsed, colorFromList(resultadoParametros.parsed.take(3))), resultadoHijo.notParsed)
  }

  def colorFromList(list: List[Int]): Color ={
    list match {
      case List(r,g,b) => Color.rgb(r,g,b)
    }
  }
}

case object parserEscala extends Parser[Escala] {
  def parse(text: String): Try[Resultado[Escala]] = {
    for {
      elemento <- string("escala").parse(text)
      resultadoParametros <- (parserParametros(double(), parserSep, 2)).parse(elemento.notParsed)
      resultadoHijo <- parserHijo().parse(resultadoParametros.notParsed)
    } yield Resultado(Escala(resultadoHijo.parsed, resultadoParametros.parsed.head, resultadoParametros.parsed(1)), resultadoHijo.notParsed)
  }
}

case object parserRotacion extends Parser[Rotacion] {
  def parse(text: String): Try[Resultado[Rotacion]] = {
    for {
      elemento <- string("rotacion").parse(text)
      resultadoParametros <- (parserParametros(double(), parserSep, 1)).parse(elemento.notParsed)
      resultadoHijo <- parserHijo().parse(resultadoParametros.notParsed)
    } yield Resultado(Rotacion(resultadoHijo.parsed, resultadoParametros.parsed.head), resultadoHijo.notParsed)
  }
}

case object parserTraslacion extends Parser[Traslacion] {
  def parse(text: String): Try[Resultado[Traslacion]] = {
//    val parametros = parserParametros(double(), parserSep, 2)
////    val parserPartes = string("traslacion[") ~> parserEspacios ~> parametros <~ parserEspacios <~ char(']') <> (char('(') ~> parserEspacios ~> parserNodo <~ parserEspacios <~ char(')') )
//    val parserPartes = parserEspacios ~> parametros <~ parserEspacios <~ char(']') <> (char('(') ~> parserEspacios ~> parserNodo <~ parserEspacios <~ char(')') )
//    val a = for {
//      elemento <- string("traslacion[").parse(text)
//    } yield parserPartes.parse(elemento.notParsed)
//
//    a.get.map(resultado => resultado.parsed match {
//      case List(tx,ty, hijo) => Resultado(Traslacion(hijo.asInstanceOf[Nodo], tx.asInstanceOf[Double], ty.asInstanceOf[Double]), resultado.notParsed)
//    })

    for {
      elemento <- string("traslacion").parse(text)
      resultadoParametros <- (parserParametros(double(), parserSep, 2)).parse(elemento.notParsed)
      resultadoHijo <- parserHijo().parse(resultadoParametros.notParsed)
    } yield Resultado(Traslacion(resultadoHijo.parsed, resultadoParametros.parsed.head, resultadoParametros.parsed(1)), resultadoHijo.notParsed)
  }
}

case object parserNodo extends Parser[Nodo]{
  def parse(text: String): Try[Resultado[Nodo]] = {
    (parserRectangulo <|> parserTriangulo <|> parserCirculo <|> parserColor <|> parserGrupo <|> parserRotacion <|> parserTraslacion <|> parserEscala).parse(text)
  }
}

case object parserGrupo extends Parser[Grupo[Nodo]] {
  def parse(text: String): Try[Resultado[Grupo[Nodo]]] = {
    for {
      elemento <- string("grupo").parse(text)
      resultado <- (parserHijos()).parse(elemento.notParsed)
    } yield Resultado(Grupo(resultado.parsed), resultado.notParsed)
  }
}

package object DrawParsers {
  def parse(text: String): Option[Nodo] = {
//    println("Parametros parseados: " + contador.cont_id)
//    val t0 = LocalDateTime.now()
//    val dt = LocalDateTime.now()
//    println(t0)
//    println(dt)
//    println("Parametros parseados: " + contador.get)
//    println("Strings parseados: " + contador.cont_id)
//
//    println("parseadooo vieja")
//    LocalDateTime.now()

    for {
      resultado <- parserNodo.parse(text).toOption
    } yield resultado.parsed

  }

  def interactiveScreen(): Unit = {
    TADPDrawingAdapter.forInteractiveScreen { (imageDescription, adapter) =>
      val result = parse(imageDescription)
      if(result.isDefined) {
        dibujar(result.get,adapter)
      }
    }
  }

  def parseAndDraw(text: String): Unit = {
    val result = parse(text)
    if(result.isDefined) {
      TADPDrawingAdapter
        .forScreen { adapter =>
          dibujar(simplificar(result.get), adapter)
        }
    }
  }

//  def parsePatternMatching(text: String): Nodo = {
//    val parserRectanguloNombre = string("rectangulo")
//    val parserTrianguloNombre = string("triangulo")
//
//    text match {
//      case "rectangulo" =>
//    }
//
//
//  }
}

