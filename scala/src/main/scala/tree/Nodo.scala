package tree

import scalafx.scene.paint.Color
import tadp._

abstract class Nodo()

// Figuras
abstract class Figura() extends Nodo()

case class Circulo(center: Punto, radius: Double) extends Figura()

case class Cuadrado(topLeft: Punto, bottomRight: Punto) extends Figura()

case class Triangulo(firstPoint: Punto, secondPoint: Punto, thirstPoint: Punto) extends Figura()

// Transformacion
abstract class Transformacion(val hijo: Nodo) extends Nodo() {
//  def copy(hijo: Nodo): Transformacion = {
//    this.copy(hijo)
//  }
}

case class Colorete(colorin: Nodo, color: Color) extends Transformacion(colorin) {
  def equals(obj: Colorete): Boolean = {
    println(this.color + " " + obj.color)
    color.equals(obj.color)
  }


}

case class Rotacion(rotin: Nodo, angulo: Double) extends Transformacion(rotin) {
  def equals(obj: Rotacion): Boolean = angulo.equals(obj.angulo)
}

case class Escala(escalin: Nodo, escaladoX: Double, escaladoY: Double) extends Transformacion(escalin) {
  def equals(obj: Escala): Boolean = escaladoX.equals(obj.escaladoX) && escaladoY.equals(obj.escaladoY)
}

case class Traslacion(trasladin: Nodo, desplazamientoX: Double, desplazamientoY: Double) extends Transformacion(trasladin) {
  def equals(obj: Traslacion): Boolean = desplazamientoX.equals(obj.desplazamientoX) && desplazamientoY.equals(obj.desplazamientoY)
}

// Grupo
case class Grupo[+T<:Nodo](hijos: List[T]) extends Nodo()