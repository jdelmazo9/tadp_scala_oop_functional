import tadp.Punto
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

  def simplificar(ast: Nodo): Nodo = {
    ast match {
      // casos a simplificar
        // transformación de color aplicada a otra transformacion de color => queda la de adentro
      case Colorete(Colorete(hijo2,color2), _)                                    => Colorete(simplificar(hijo2), color2)
        // transformación aplicada a todos los hijos de un grupo => transformacion aplicada al grupo
      case Grupo(listaTransformaciones)
          if listaTransformaciones.tail.forall(transformin => transformin.equals(listaTransformaciones.head))
                                                                                  => listaTransformaciones.head.asInstanceOf[Transformacion].transfCopy(hijo = Grupo(listaTransformaciones.map(transfo => simplificar(transfo.asInstanceOf[Transformacion].hijo))))


        //Si tenemos una rotación, escala o traslación que contiene a otra transformación del mismo tipo, queremos reemplazarlas por la unión de las transformaciones.
        // Para cada una de estas transformaciones, la forma de unirse es la siguiente:
        //Rotación: se suman los grados de ambas rotaciones.
      case Rotacion(Rotacion(hijo2, angulo2), angulo)                             => Rotacion(simplificar(hijo2),angulo+angulo2)
        //Escala: se multiplican los factores de escalado.
      case Escala(Escala(hijo2, escaladoX_2, escaladoY_2),escaladoX, escaladoY)   => Escala(simplificar(hijo2),escaladoX*escaladoX_2, escaladoY*escaladoY_2)
        //Traslación: se suman los valores de las traslaciones.
      case Traslacion(Traslacion(hijo2, desplazamientoX_2, desplazamientoY_2),desplazamientoX, desplazamientoY)
                                                                                  => Traslacion(simplificar(hijo2),desplazamientoX*desplazamientoX_2, desplazamientoY*desplazamientoY_2)
        //Hay veces donde una rotación, escala o traslación no cambia de ninguna manera a al elemento al que se aplican, en esos casos queremos simplemente reemplazarlas por el elemento.
        // Las transformaciones nulas que queremos borrar son:
        //rotación de 0 grados
      case Rotacion(hijo, 0)                                                      => simplificar(hijo)
        //escala de 1 en x, 1 en y
      case Escala(hijo, 1, 1)                                                     => simplificar(hijo)
      //traslación de 0 en x, 0 en y
      case Traslacion(hijo, 0, 0)                                                 => simplificar(hijo)

      case Rotacion(hijo, grados)                                                 => Rotacion(simplificar(hijo),grados)
      case Colorete(hijo, color)                                                  => Colorete(simplificar(hijo), color)
      case Escala(hijo, escaladoX, escaladoY)                                     => Escala(simplificar(hijo),escaladoX, escaladoY)
      case Traslacion(hijo, desplazamientoX, desplazamientoY)                     => Traslacion(simplificar(hijo),desplazamientoX, desplazamientoY)
      case Grupo(listaNodos)                                                      => Grupo(listaNodos.map(nodo => simplificar(nodo)))
      case cualquierOtro                                                          => cualquierOtro
    }
  }
}