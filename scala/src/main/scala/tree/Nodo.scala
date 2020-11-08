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
abstract class Transformacion(hijo: Nodo) extends Nodo()

case class Colorete(hijo: Nodo, color: Color) extends Transformacion(hijo)

case class Rotacion(hijo: Nodo, angulo: Double) extends Transformacion(hijo)

case class Escala(hijo: Nodo, escaladoX: Double, escaladoY: Double) extends Transformacion(hijo)

case class Traslacion(hijo: Nodo, desplazamientoX: Double, desplazamientoY: Double) extends Transformacion(hijo)

// Grupo
case class Grupo(hijos: List[Nodo]) extends Nodo()