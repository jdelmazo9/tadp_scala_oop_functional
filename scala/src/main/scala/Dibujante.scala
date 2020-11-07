import tadp.internal.TADPDrawingAdapter
import tree._

package object Dibujante {
  def dibujar(ast: Nodo, adapterInicial: TADPDrawingAdapter): TADPDrawingAdapter = {
    ast match {
      // Figuras
      case Cuadrado(topLeft, bottomRight) => adapterInicial.copy().rectangle(topLeft.position(), bottomRight.position())

      // Transformaciones
      case Colorete(hijo, color)          => dibujar(hijo,adapterInicial.copy().beginColor(color))

      // Grupo
      case Grupo(hijos)                   => hijos.foldLeft(adapterInicial) { case (adapter, nodo) => dibujar(nodo, adapter) }
    }
  }
}