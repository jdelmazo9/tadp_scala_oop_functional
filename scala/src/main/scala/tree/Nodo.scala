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
  def transfCopy(hijo: Nodo): Transformacion

  override def equals(obj: Any): Boolean = {
    this.transfEquals(obj)
  }
  def transfEquals(obj: Any): Boolean
}

case class Colorete(colorin: Nodo, color: Color) extends Transformacion(colorin) {
  override def transfEquals(obj: Any): Boolean = {
    obj.isInstanceOf[Colorete] && color.equals(obj.asInstanceOf[Colorete].color)
  }
  override def transfCopy(hijo: Nodo): Colorete = {
    this.copy(colorin = hijo)
  }
}

case class Rotacion(rotin: Nodo, angulo: Double) extends Transformacion(rotin) {
  override def transfEquals(obj: Any): Boolean = {
    obj.isInstanceOf[Rotacion] && angulo.equals(obj.asInstanceOf[Rotacion].angulo)
  }
  override def transfCopy(hijo: Nodo): Rotacion = {
    this.copy(rotin = hijo)
  }
}

case class Escala(escalin: Nodo, escaladoX: Double, escaladoY: Double) extends Transformacion(escalin) {
  override def transfEquals(obj: Any): Boolean = {
    obj.isInstanceOf[Escala] && escaladoX.equals(obj.asInstanceOf[Escala].escaladoX) && escaladoY.equals(obj.asInstanceOf[Escala].escaladoY)
  }
  override def transfCopy(hijo: Nodo): Escala = {
    this.copy(escalin = hijo)
  }
}

case class Traslacion(trasladin: Nodo, desplazamientoX: Double, desplazamientoY: Double) extends Transformacion(trasladin) {
  override def transfEquals(obj: Any): Boolean = {
    obj.isInstanceOf[Traslacion] && desplazamientoX.equals(obj.asInstanceOf[Traslacion].desplazamientoX) && desplazamientoY.equals(obj.asInstanceOf[Traslacion].desplazamientoY)
  }
  override def transfCopy(hijo: Nodo): Traslacion = {
    this.copy(trasladin = hijo)
  }
}

// Grupo
case class Grupo[+T<:Nodo](hijos: List[T]) extends Nodo()