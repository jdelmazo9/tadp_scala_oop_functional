package tree

import scalafx.scene.paint.Color
import tadp._
import tadp.internal.TADPDrawingAdapter

abstract class Nodo(){
}

// Figuras
abstract class Figura() extends Nodo(){
  def dibujeMaestro(adapter: TADPDrawingAdapter): TADPDrawingAdapter
}

case class Circulo(center: Punto, radius: Double) extends Figura(){
  def dibujeMaestro(adapter: TADPDrawingAdapter): TADPDrawingAdapter = {
    adapter.circle(center.position(), radius)
  }
}

case class Cuadrado(topLeft: Punto, bottomRight: Punto) extends Figura(){
  def dibujeMaestro(adapter: TADPDrawingAdapter): TADPDrawingAdapter = {
    adapter.rectangle(topLeft.position(), bottomRight.position())
  }
}

// Transformacion
abstract class Transformacion(hijo: Nodo) extends Nodo(){
  def dibujeMaestro(adapter: TADPDrawingAdapter): TADPDrawingAdapter
}

// @TODO esto es re funcionaloso
case class Colorete(hijo: Nodo, color: Color) extends Transformacion(hijo){
  def dibujeMaestro(adapter: TADPDrawingAdapter): TADPDrawingAdapter = {
    val a = adapter.beginColor(color)
    val b = hijo.dibujeMaestro(a)
    b.end()
  }
}
