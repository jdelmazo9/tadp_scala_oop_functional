import Dibujante.dibujar
import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter
import tree._

case object parseadas extends App {
  val cuadrado = parserCuadrado.parse("cuadrado[150 @ 100,200 @ 300]").get.parsed
  val cuadrado2 = parserCuadrado.parse("cuadrado[250 @ 200,400 @ 500]").get.parsed
  val cuadrado3 = parserCuadrado.parse("cuadrado[450 @ 200,475 @ 225]").get.parsed
  val grupo = Grupo(List(cuadrado, cuadrado2, cuadrado3))
  val color = Colorete(grupo, Color.rgb(200,200,200))
  TADPDrawingAdapter
    .forScreen { adapter =>
      dibujar(color, adapter)
    }
}