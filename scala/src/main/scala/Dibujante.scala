import tadp.internal.TADPDrawingAdapter
import tree._

package object Dibujante {
  def dibujar(ast: Nodo, adapterInicial: TADPDrawingAdapter): TADPDrawingAdapter = {
    ast match {
      // Figuras
      case Cuadrado(topLeft, bottomRight)                         => adapterInicial.copy().rectangle(topLeft.position(), bottomRight.position())
      case Triangulo(firstPoint, secondPoint, thirstPoint)        => adapterInicial.copy().triangle(firstPoint.position(), secondPoint.position(), thirstPoint.position())
      case Circulo(center, radius)                                => adapterInicial.copy().circle(center.position(), radius)

      // Transformaciones
      case Colorete(hijo, color)                                  => dibujar(hijo,adapterInicial.copy().beginColor(color)).end()
      case Escala(hijo, escaladoEnX, escaladoEnY)                 => dibujar(hijo,adapterInicial.copy().beginScale(escaladoEnX,escaladoEnY)).end()
      case Rotacion(hijo, angulo)                                 => dibujar(hijo,adapterInicial.copy().beginRotate(angulo)).end()
      case Traslacion(hijo, desplazamientoX, desplazamientoY)     => dibujar(hijo,adapterInicial.copy().beginTranslate(desplazamientoX, desplazamientoY)).end()

      // Grupo
      case Grupo(hijos)                   => hijos.foldLeft(adapterInicial) { case (adapter, nodo) => dibujar(nodo, adapter) }
    }
  }

//  def simplificar(ast: Nodo): Nodo = {
//    ast match {
//      // Figuras
//      case Cuadrado(topLeft, bottomRight)                         => adapterInicial.copy().rectangle(topLeft.position(), bottomRight.position())
//      case Triangulo(firstPoint, secondPoint, thirstPoint)        => adapterInicial.copy().triangle(firstPoint.position(), secondPoint.position(), thirstPoint.position())
//      case Circulo(center, radius)                                => adapterInicial.copy().circle(center.position(), radius)
//
//      // Transformaciones
//      case Colorete(hijo, color)                                  => dibujar(hijo,adapterInicial.copy().beginColor(color)).end()
//      case Escala(hijo, escaladoEnX, escaladoEnY)                 => dibujar(hijo,adapterInicial.copy().beginScale(escaladoEnX,escaladoEnY)).end()
//      case Rotacion(hijo, angulo)                                 => dibujar(hijo,adapterInicial.copy().beginRotate(angulo)).end()
//      case Traslacion(hijo, desplazamientoX, desplazamientoY)     => dibujar(hijo,adapterInicial.copy().beginTranslate(desplazamientoX, desplazamientoY)).end()
//
//      // Grupo
//      case Grupo(hijos)                   => hijos.foldLeft(adapterInicial) { case (adapter, nodo) => dibujar(nodo, adapter) }
//    }
//  }
}