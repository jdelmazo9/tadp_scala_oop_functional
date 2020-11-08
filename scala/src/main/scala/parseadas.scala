import Dibujante.dibujar
import parser.{parserRectangulo}
import scalafx.scene.paint.Color
import tadp.internal.TADPDrawingAdapter
import tree._

case object parseadas extends App {
  val cuadrado = parserRectangulo.parse("cuadrado[150 @ 100,200 @ 300]").get.parsed
  val cuadrado2 = parserRectangulo.parse("cuadrado[250 @ 200,400 @ 500]").get.parsed
  val cuadrado3 = parserRectangulo.parse("cuadrado[450 @ 200,475 @ 225]").get.parsed
//  val grupo = Grupo(List(cuadrado, cuadrado2))
  val color = Colorete(Grupo(List(cuadrado, cuadrado2)), Color.rgb(200,0,0))
  val grupo2 = Grupo(List(color,cuadrado3))
  TADPDrawingAdapter
    .forScreen { adapter =>
      dibujar(grupo2, adapter)
    }
}